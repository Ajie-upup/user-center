package com.ajie.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ajie.usercenter.model.domain.User;
import com.ajie.usercenter.service.UserService;
import com.ajie.usercenter.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author Ajie
* @description 针对表【user】的数据库操作Service实现
* @createDate 2022-11-06 19:39:04
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
}




