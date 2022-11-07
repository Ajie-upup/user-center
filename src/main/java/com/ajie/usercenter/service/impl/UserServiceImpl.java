package com.ajie.usercenter.service.impl;

import com.ajie.usercenter.mapper.UserMapper;
import com.ajie.usercenter.model.domain.User;
import com.ajie.usercenter.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ajie.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author Ajie
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2022-11-06 19:39:04
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    private static final String SALT = "ajie";

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
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
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

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.校验用户的账户、密码、校验密码，是否符合要求
        // 1.1.非空校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        // 1.2. 账户长度不小于4位
        if (userAccount.length() < 4) {
            return null;
        }
        // 1.3. 密码就不小于8位
        if (userPassword.length() < 8) {
            return null;
        }
        // 1.4. 账户不包含特殊字符
        String validRule = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%…… &*（）——+ | {}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validRule).matcher(userAccount);
        // 如果包含非法字符，则返回
        if (matcher.find()) {
            return null;
        }
        //2. 校验用户登录密码是否正确，和数据库中的密文密码比对
        String updatePassword = DigestUtils.md5DigestAsHex((SALT + userPassword)
                .getBytes(StandardCharsets.UTF_8));
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", userPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("user login failed, userAccount cannot match the userPassword");
            return null;
        }
        //3、用户信息脱敏，隐藏敏感信息，防止数据库中的字段泄露
        User safetyUser = getSafetyUser(user);

        //4、记录用户的登录态，保存到服务器中
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    /**
     * 用户信息脱敏，隐藏敏感信息，防止数据库中的字段泄露
     *
     * @param originUser 未脱敏用户
     * @return 脱敏用户
     */
    @Override
    public User getSafetyUser(User originUser) {
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        return safetyUser;
    }
}




