package com.example.aics.controller;

import com.example.aics.common.Result;
import com.example.aics.context.BaseContext;
import com.example.aics.dto.user.UserLoginDTO;
import com.example.aics.dto.user.UserRegisterDTO;
import com.example.aics.entity.User;
import com.example.aics.service.UserService;
import com.example.aics.utils.JwtUtil;
import com.example.aics.vo.user.UserVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public Result<UserVO> register(@RequestBody @Valid UserRegisterDTO userRegisterDTO) {
        User user = userService.register(userRegisterDTO.getUsername(), userRegisterDTO.getPassword(), userRegisterDTO.getNickname());
        UserVO userVO = convertToVO(user);
        return Result.success(userVO);
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        String token = userService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword());
        return Result.success(token);
    }

    @GetMapping("/info")
    public Result<UserVO> getUserInfo() {
        Long userId = BaseContext.getCurrentId();
        User user = userService.getById(userId);
        return Result.success(convertToVO(user));
    }
    private UserVO convertToVO(User user) {
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setNickname(user.getNickname());
        userVO.setEmail(user.getEmail());
        userVO.setPhone(user.getPhone());
        userVO.setAvatar(user.getAvatar());
        userVO.setRole(user.getRole());
        return userVO;
    }

}