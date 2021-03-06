package me.atm.service.impl;
import java.util.Date;

import me.atm.common.enums.SexEnum;
import me.atm.common.utils.DateUtils;
import me.atm.common.utils.MD5Utils;
import me.atm.mapper.UsersMapper;
import me.atm.pojo.Users;
import me.atm.pojo.bo.UserBO;
import me.atm.service.UsersService;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @author Altman
 * @date 2019/11/05
 **/
@Service
public class UsersServiceImpl implements UsersService {

    private static String USER_FACE_URL = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";

    @Resource
    private Sid sid;

    @Resource
    private UsersMapper usersMapper;

    @Override
    public boolean queryUsernameIsExist(String username) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        Users users = usersMapper.selectOneByExample(example);
        return users != null ? true : false;
    }

    @Override
    public Users createUser(UserBO userBO) {
        Users users = new Users();
        users.setId(sid.nextShort());
        users.setUsername(userBO.getUsername());
        try {
            users.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 默认用户昵称同用户名
        users.setNickname(userBO.getUsername());
        // 默认用户头像
        users.setFace(USER_FACE_URL);
        // 默认性别为保密
        users.setSex(SexEnum.secret.type);
        // 默认生日为 1970-01-01
        users.setBirthday(DateUtils.stringToDate("1970-01-01"));
        users.setCreatedTime(new Date());
        users.setUpdatedTime(new Date());

        usersMapper.insert(users);

        return users;
    }

    @Override
    public Users queryUserForLogin(String username, String password) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        criteria.andEqualTo("password", password);
        return usersMapper.selectOneByExample(example);
    }
}
