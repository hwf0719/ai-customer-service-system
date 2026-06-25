package com.example.aics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.aics.entity.User;

public interface UserService extends IService<User> {

    /**
     * 用户注册（普通用户，角色固定为 USER）
     */
    User register(String username, String password, String nickname);

    /**
     * 管理员创建用户（可指定角色）
     */
    User createUser(String username, String password, String nickname, String role);

    /**
     * 用户登录
     */
    User login(String username, String password);

    /**
     * 根据用户名查找用户
     */
    User findByUsername(String username);
}