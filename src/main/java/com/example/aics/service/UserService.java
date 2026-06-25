package com.example.aics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.aics.entity.User;

public interface UserService extends IService<User> {

    /**
     * 用户注册
     */
    User register(String username, String password, String nickname);

    /**
     * 用户登录
     */
    String login(String username, String password);

    /**
     * 根据用户名查找用户
     */
    User findByUsername(String username);
}