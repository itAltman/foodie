package me.atm.foodie.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.atm.common.utils.JsonResult;
import me.atm.pojo.bo.UserBO;
import me.atm.service.UsersService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
    public JsonResult register(@RequestBody UserBO userBO) {
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
        usersService.createUser(userBO);

        return JsonResult.ok();
    }
}
