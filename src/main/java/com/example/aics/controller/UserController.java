package com.example.aics.controller;

import com.example.aics.common.Result;
import com.example.aics.context.BaseContext;
import com.example.aics.dto.user.AdminCreateUserDTO;
import com.example.aics.dto.user.UserLoginDTO;
import com.example.aics.dto.user.UserRegisterDTO;
import com.example.aics.entity.User;
import com.example.aics.service.UserService;
import com.example.aics.utils.JwtUtil;
import com.example.aics.vo.user.LoginVO;
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
    public Result<LoginVO> login(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        // 登录获取用户信息和 token
        User user = userService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword());
        // 生成包含用户ID和角色的token
        String token = jwtUtil.generateToken(user.getId(), user.getRole());

        // 构建返回对象
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUser(convertToVO(user));

        return Result.success(loginVO);
    }

    @GetMapping("/info")
    public Result<UserVO> getUserInfo() {
        Long userId = BaseContext.getCurrentId();
        User user = userService.getById(userId);
        return Result.success(convertToVO(user));
    }

    @PostMapping("/admin/create")
    public Result<UserVO> createUserByAdmin(@RequestBody @Valid AdminCreateUserDTO dto) {
        // 验证当前用户是否是管理员
        String currentRole = BaseContext.getCurrentRole();
        if (!"ADMIN".equals(currentRole)) {
            return Result.error(403, "权限不足，仅管理员可创建用户");
        }

        // 管理员可以指定角色
        User user = userService.createUser(
                dto.getUsername(),
                dto.getPassword(),
                dto.getNickname(),
                dto.getRole()
        );
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