package com.ajie.usercenter.service;

import com.ajie.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @BelongsProject: user-center
 * @BelongsPackage: com.ajie.usercenter
 * @Author: ajie
 * @Date: 2022/11/6 19:42
 * @Description: 用户服务测试
 */
@SpringBootTest
class UserServiceTest {
    @Resource
    public UserService userService;

    @Test
    void testAddUser() {
        User user = new User();
        user.setUsername("ajie");
        user.setUserAccount("ajie123");
        user.setAvatarUrl("https://pics3.baidu.com/feed/cf1b9d16fdfaaf51055aeb850e4a2de7f01f7a73.jpeg@f_auto?token=13a989ae26c8c26689240bf4a3c776a0");
        user.setGender(0);
        user.setUserPassword("123456");
        user.setPhone("111111");
        user.setEmail("222222@qq.com");
        user.setUserStatus(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDelete(0);
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }
}