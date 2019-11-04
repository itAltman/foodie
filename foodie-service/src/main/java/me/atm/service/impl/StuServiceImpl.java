package me.atm.service.impl;

import me.atm.mapper.StuMapper;
import me.atm.pojo.Stu;
import me.atm.service.IStuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Altman
 * @date 2019/11/03
 **/
@Service
public class StuServiceImpl implements IStuService {

    @Autowired
    private StuMapper stuMapper;

    @Override
    public Stu selectOne(int id) {
        return stuMapper.selectByPrimaryKey(id);
    }
}
