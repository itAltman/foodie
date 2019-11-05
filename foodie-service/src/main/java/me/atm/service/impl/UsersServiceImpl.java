package me.atm.service.impl;
import java.util.Date;

import me.atm.common.enums.Sex;
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

    private static String USER_FACE_URL = "https://b-ssl.duitang.com/uploads/item/201806/07/20180607185957_fjrFt.thumb.200_0.jpeg";

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
        users.setSex(Sex.secret.type);
        // 默认生日为 1970-01-01
        users.setBirthday(DateUtils.stringToDate("1970-01-01"));
        users.setCreatedTime(new Date());
        users.setUpdatedTime(new Date());

        usersMapper.insert(users);

        return users;
    }
}
