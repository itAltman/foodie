package me.atm.foodie.controller;

import java.util.Date;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.atm.common.utils.CookieUtils;
import me.atm.common.utils.JsonResult;
import me.atm.common.utils.JsonUtils;
import me.atm.common.utils.MD5Utils;
import me.atm.pojo.Users;
import me.atm.pojo.bo.UserBO;
import me.atm.service.UsersService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登录模块
 *
 * @author Altman
 * @date 2019/11/05
 **/
@Api(value = "注册登录", tags = {"注册登录"})
@RestController
@RequestMapping("/passport")
public class PassportController {

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

        // 6. 有一些是不能返回到前端的，比如密码等敏感信息
        processFieldToNull(user);

        // 7. 用户信息存到 cookie
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(user), true);
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

        // 3. 有一些是不能返回到前端的，比如密码等敏感信息
        processFieldToNull(user);

        // 4. 用户信息存到 cookie
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(user), true);
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

        // TODO 用户退出登录，需要清空购物车
        // TODO 分布式会话中需要清除用户数据

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
    private void processFieldToNull(Users user) {
        user.setPassword(null);
        user.setRealname(null);
        user.setMobile(null);
        user.setEmail(null);
        user.setBirthday(null);
        user.setCreatedTime(null);
    }
}
