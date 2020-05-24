package me.atm.foodie.controller;

import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.atm.common.utils.CookieUtils;
import me.atm.common.utils.JsonResult;
import me.atm.common.utils.JsonUtils;
import me.atm.common.utils.MD5Utils;
import me.atm.pojo.Users;
import me.atm.pojo.bo.ShopcartBO;
import me.atm.pojo.bo.UserBO;
import me.atm.pojo.vo.UsersVO;
import me.atm.service.UsersService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户登录模块
 *
 * @author Altman
 * @date 2019/11/05
 **/
@Api(value = "注册登录", tags = {"注册登录"})
@RestController
@RequestMapping("/passport")
public class PassportController extends BaseController {

    @Resource
    private UsersService usersService;

    @ApiOperation(value = "检查用户名", notes = "检查用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public JsonResult usernameIsExist(@RequestParam String username) {
        // 1. 用户名是否为空
        if (StringUtils.isBlank(username)) {
            return JsonResult.errorMsg("用户名不能为空");
        }

        // 2. 用户名是否存在
        boolean isExist = usersService.queryUsernameIsExist(username);
        if (isExist) {
            return JsonResult.errorMsg("用户名已经存在");
        }

        return JsonResult.ok();
    }

    @ApiOperation(value = "用户注册", notes = "用于网站用户注册", httpMethod = "POST")
    @PostMapping("/register")
    public JsonResult register(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String verifyPassword = userBO.getVerifyPassword();

        // 1. 用户名、密码不为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(verifyPassword)) {
            return JsonResult.errorMsg("用户名或密码不能为空");
        }

        // 2. 查询用户名是否存在
        boolean isExist = usersService.queryUsernameIsExist(username);
        if (isExist) {
            return JsonResult.errorMsg("用户名已经存在");
        }

        // 3. 密码长度不能小于6位
        if (password.length() < 6) {
            return JsonResult.errorMsg("密码长度不能小于6");
        }

        // 4. 判断两次密码是否一致
        if (!password.equals(verifyPassword)) {
            return JsonResult.errorMsg("两次密码输入不一致");
        }

        // 5. 实现注册
        Users user = usersService.createUser(userBO);

        // 6. 生成token信息并存储到 redis 中
        UsersVO usersVO = convertUsersVO(user);

        // 7. 用户信息存到 cookie
        user = processFieldToNull(user);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(usersVO), true);
        synchShopcartData(user.getId(), request, response);
        return JsonResult.ok();
    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public JsonResult login(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = userBO.getUsername();
        String password = userBO.getPassword();

        // 1. 验证参数
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return JsonResult.errorMsg("用户名或密码不能为空");
        }

        // 2. 登录
        Users user = usersService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
        if (user == null) {
            return JsonResult.errorMsg("用户名或密码错误，请检验之后重试！");
        }

        // 3. 生成token信息并存储到 redis 中
        UsersVO usersVO = convertUsersVO(user);

        // 4. 用户信息存到 cookie
        user = processFieldToNull(user);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(usersVO), true);

        // TODO 生成用户token，存入redis会话
        synchShopcartData(user.getId(), request, response);
        return JsonResult.ok();
    }

    @ApiOperation(value = "退出登录", notes = "退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public JsonResult login(@RequestParam String userId, HttpServletRequest request, HttpServletResponse response) {
        // 1. 验证参数
        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("用户id不能为空");
        }

        // 2. 删除 cookie
        CookieUtils.deleteCookie(request, response, "user");

        // 用户退出登录，需要清空购物车
        CookieUtils.deleteCookie(request, response, FOODIE_SHOPCART);

        // 分布式会话中需要清除用户数据
        redisOperator.del(REDIS_USER_TOKEN + ":" + userId);

        return JsonResult.ok();
    }

    /**
     * 将敏感字段置空
     *
     * @param user : 用户信息
     * @return 原用户
     * @author Altman
     * @date 2019-11-06
     */
    private Users processFieldToNull(Users user) {
        user.setPassword(null);
        user.setRealname(null);
        user.setMobile(null);
        user.setEmail(null);
        user.setBirthday(null);
        user.setCreatedTime(null);
        return user;
    }

    /**
     * 注册登录成功后，同步cookie和redis中的购物车数据
     */
    private void synchShopcartData(String userId, HttpServletRequest request,
                                   HttpServletResponse response) {

        /**
         * 1. redis中无数据，如果cookie中的购物车为空，那么这个时候不做任何处理
         *                 如果cookie中的购物车不为空，此时直接放入redis中
         * 2. redis中有数据，如果cookie中的购物车为空，那么直接把redis的购物车覆盖本地cookie
         *                 如果cookie中的购物车不为空，
         *                      如果cookie中的某个商品在redis中存在，
         *                      则以cookie为主，删除redis中的，
         *                      把cookie中的商品直接覆盖redis中（参考京东）
         * 3. 同步到redis中去了以后，覆盖本地cookie购物车的数据，保证本地购物车的数据是同步最新的
         */

        // 从redis中获取购物车
        String shopcartJsonRedis = redisOperator.get(FOODIE_SHOPCART + ":" + userId);

        // 从cookie中获取购物车
        String shopcartStrCookie = CookieUtils.getCookieValue(request, FOODIE_SHOPCART, true);

        if (StringUtils.isBlank(shopcartJsonRedis)) {
            // redis为空，cookie不为空，直接把cookie中的数据放入redis
            if (StringUtils.isNotBlank(shopcartStrCookie)) {
                redisOperator.set(FOODIE_SHOPCART + ":" + userId, shopcartStrCookie);
            }
        } else {
            // redis不为空，cookie不为空，合并cookie和redis中购物车的商品数据（同一商品则覆盖redis）
            if (StringUtils.isNotBlank(shopcartStrCookie)) {

                /**
                 * 1. 已经存在的，把cookie中对应的数量，覆盖redis（参考京东）
                 * 2. 该项商品标记为待删除，统一放入一个待删除的list
                 * 3. 从cookie中清理所有的待删除list
                 * 4. 合并redis和cookie中的数据
                 * 5. 更新到redis和cookie中
                 */

                List<ShopcartBO> shopcartListRedis = JsonUtils.jsonToList(shopcartJsonRedis, ShopcartBO.class);
                List<ShopcartBO> shopcartListCookie = JsonUtils.jsonToList(shopcartStrCookie, ShopcartBO.class);

                // 定义一个待删除list
                List<ShopcartBO> pendingDeleteList = Lists.newArrayList();

                for (ShopcartBO redisShopcart : shopcartListRedis) {
                    String redisSpecId = redisShopcart.getSpecId();

                    for (ShopcartBO cookieShopcart : shopcartListCookie) {
                        String cookieSpecId = cookieShopcart.getSpecId();

                        if (redisSpecId.equals(cookieSpecId)) {
                            // 覆盖购买数量，不累加，参考京东
                            redisShopcart.setBuyCounts(cookieShopcart.getBuyCounts());
                            // 把cookieShopcart放入待删除列表，用于最后的删除与合并
                            pendingDeleteList.add(cookieShopcart);
                        }

                    }
                }

                // 从现有cookie中删除对应的覆盖过的商品数据
                shopcartListCookie.removeAll(pendingDeleteList);

                // 合并两个list
                shopcartListRedis.addAll(shopcartListCookie);
                // 更新到redis和cookie
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JsonUtils.objectToJson(shopcartListRedis), true);
                redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartListRedis));
            } else {
                // redis不为空，cookie为空，直接把redis覆盖cookie
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, shopcartJsonRedis, true);
            }

        }
    }

}
