package com.lizk.jwt.demo.entity;

/**
 * 文件名
 * Created at 2020/3/9
 * Created by lizongke
 * Copyright (C) 2020 SAIC VOLKSWAGEN, All rights reserved.
 */
public class User {
    private String userName;
    private String passWord;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public User() {
    }

    public User(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }
}
