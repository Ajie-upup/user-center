package com.ajie.usercenter.service.impl;

import com.ajie.usercenter.mapper.UserMapper;
import com.ajie.usercenter.model.domain.User;
import com.ajie.usercenter.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ajie
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2022-11-06 19:39:04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1.校验用户的账户、密码、校验密码，是否符合要求
        // 1.1.非空校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return (long) -1;
        }
        // 1.2. 账户长度不小于4位
        if (userAccount.length() < 4) {
            return (long) -1;
        }
        // 1.3. 密码就不小于8位
        if (userPassword.length() < 8) {
            return (long) -1;
        }
        // 1.4. 账户不包含特殊字符
        String validRule = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%…… &*（）——+ | {}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validRule).matcher(userAccount);
        // 如果包含非法字符，则返回
        if (matcher.find()) {
            return (long) -1;
        }
        // 1.5. 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return (long) -1;
        }
        // 1.6. 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return (long) -1;
        }
        // 2.对密码进行加密（密码千万不要直接以明文存储到数据库中）
        final String SALT = "ajie";
        String updatePassword = DigestUtils.md5DigestAsHex((SALT + userPassword)
                .getBytes(StandardCharsets.UTF_8));
        // 3. 向数据库插入用户数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(updatePassword);
        //返回受影响的行数
        int res = userMapper.insert(user);
        if (res < 0) {
            return (long) -1;
        }
        return user.getId();
    }
}




