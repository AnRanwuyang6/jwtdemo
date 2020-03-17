package com.lizk.jwt.demo.entity;

import java.io.Serializable;

/**
 * Created by lizk on 2017/4/20.
 */

public class Result implements Serializable {
    public static final String RESULT_CODE_0000 = "0000";
    public static final String RESULT_CODE_0001 = "0001";
    private String code;
    private String message;
    private Object data;

    public Result() {
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

