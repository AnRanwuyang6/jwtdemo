package com.lizk.jwt.demo.service;

import com.lizk.jwt.demo.entity.User;
import org.springframework.stereotype.Service;

/**
 * 文件名
 * Created at 2020/3/9
 * Created by lizongke
 * Copyright (C) 2020 SAIC VOLKSWAGEN, All rights reserved.
 */
@Service
public class UserServiceImpl {
    public User findUser(String userName, String passWord){
        if(userName.equals("lizk")){
            return new User("lizk","123456");
        }else{
            return new User();
        }
    }
}
