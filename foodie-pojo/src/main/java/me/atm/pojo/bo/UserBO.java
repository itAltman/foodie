package me.atm.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户对象 BO
 *
 * @author Altman
 * @date 2019/11/05
 **/
@ApiModel(value = "用户注册BO", description = "用于传输用户输入的注册信息")
public class UserBO {
    @ApiModelProperty(value = "用户名", name = "username", required = true, example = "张全蛋")
    private String username;
    @ApiModelProperty(value = "密码", name = "password", required = true, example = "222222")
    private String password;
    @ApiModelProperty(value = "确认密码", name = "verifyPassword", required = true, example = "222222")
    private String verifyPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }
}
