package com.taotao.sso.service;

import com.taotao.sso.mapper.UserMapper;
import com.taotao.sso.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public Boolean check(String param, Integer type) {
        if(type < 1 || type > 3){
            return null;
        }
        User record = new User();
        switch (type){
            case 1:
                record.setUsername(param);
                break;
            case 2:
                record.setPhone(param);
                break;
            case 3:
                record.setEmail(param);
                break;
            default:
                break;
        }
        return this.userMapper.selectOne(record) == null;
    }
}