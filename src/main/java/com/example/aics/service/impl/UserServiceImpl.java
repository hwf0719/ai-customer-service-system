package com.example.aics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.aics.entity.User;
import com.example.aics.constant.MessageConstant;
import com.example.aics.exception.BusinessException;
import com.example.aics.mapper.UserMapper;
import com.example.aics.service.UserService;
import com.example.aics.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public User register(String username, String password, String nickname) {
        User exsitingUser=findByUsername(username);
        if (exsitingUser != null){
            throw new BusinessException(MessageConstant.ALREADY_EXIST);
        }
        User user=User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .role("USER")
                .build();
        save(user);
        return user;
    }

    @Override
    public String login(String username, String password) {
        User exsitingUser = findByUsername(username);
        if (exsitingUser == null){
            throw new BusinessException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        if (!exsitingUser.getPassword().equals(password)){
            throw new BusinessException(MessageConstant.PASSWORD_ERROR);
        }
        return jwtUtil.generateToken(exsitingUser.getId());
    }

    @Override
    public User findByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username);
        return this.getOne(queryWrapper);
    }
}
