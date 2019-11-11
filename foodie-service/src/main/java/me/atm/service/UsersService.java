package me.atm.service;

import me.atm.pojo.Users;
import me.atm.pojo.bo.UserBO;

/**
 * 用户业务类
 *
 * @author Altman
 * @create 2019/11/05
 **/
public interface UsersService {
    /**
     * 判断用户名是否存在
     *
     * @param username : 用户名
     * @return true - 存在 、 false - 不存在
     * @author Altman
     * @date 2019-11-05
     */
    boolean queryUsernameIsExist(String username);

    /**
     * 创建用户信息
     *
     * @param userBO : 用户信息
     * @return 创建成功的用户信息
     * @author Altman
     * @date 2019-11-05
     */
    Users createUser(UserBO userBO);

    /**
     * 验证用户信息，登录
     *
     * @param username : 用户名
     * @param password : 密码
     * @return 用户的信息
     * @author Altman
     * @date 2019-11-05
     */
    Users queryUserForLogin(String username, String password);
}
