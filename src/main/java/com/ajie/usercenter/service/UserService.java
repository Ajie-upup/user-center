package com.ajie.usercenter.service;

import com.ajie.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Ajie
 * @description 针对表【user】的数据库操作Service
 * @createDate 2022-11-06 19:39:04
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    Long userRegister(String userAccount, String userPassword, String checkPassword);
}
