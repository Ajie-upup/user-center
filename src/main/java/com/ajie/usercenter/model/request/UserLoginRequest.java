package com.ajie.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @Author: ajie
 * @Date: 2022/11/7
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userAccount;
    private String userPassword;
}
