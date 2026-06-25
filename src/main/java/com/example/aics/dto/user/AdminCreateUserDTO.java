package com.example.aics.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminCreateUserDTO {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度3-20位")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度6-20位")
    private String password;

    private String nickname;

    @NotBlank(message = "角色不能为空")
    @Pattern(regexp = "^(ADMIN|AGENT|USER)$", message = "角色只能是 ADMIN、AGENT 或 USER")
    private String role;
}
