package com.ajie.usercenter.service;

import com.ajie.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Ajie
 * @description 针对表【user】的数据库操作Service
 * @createDate 2022-11-06 19:39:04
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 注册用户 id
     */
    Long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param request 请求
     * @return 登录用户
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户信息脱敏，隐藏敏感信息，防止数据库中的字段泄露
     *
     * @param originUser 未脱敏用户
     * @return 脱敏用户
     */
    User getSafetyUser(User originUser);
}
