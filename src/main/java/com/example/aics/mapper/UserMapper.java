package com.example.aics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.aics.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}