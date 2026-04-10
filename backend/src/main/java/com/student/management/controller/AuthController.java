package com.student.management.controller;

import com.student.management.common.Result;
import com.student.management.service.SystemConfigService;
import com.student.management.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证管理控制器
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "认证管理", description = "管理员登录、退出与密码修改接口")
public class AuthController {

    private final TokenService tokenService;
    private final SystemConfigService systemConfigService;

    @Value("${admin.username}")
    private String adminUsername;

    @PostMapping("/login")
    @Operation(summary = "管理员登录")
    public ResponseEntity<Result<Map<String, Object>>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (adminUsername.equals(username) && systemConfigService.getAdminPassword().equals(password)) {
            String token = tokenService.createToken(username);
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("username", username);
            return Result.success("登录成功", data).toResponseEntity();
        }

        return Result.<Map<String, Object>>fail(401, "用户名或密码错误").toResponseEntity();
    }

    @PostMapping("/logout")
    @Operation(summary = "管理员退出")
    public ResponseEntity<Result<Void>> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            tokenService.removeToken(authHeader.substring(7));
        }
        return Result.<Void>success("退出成功").toResponseEntity();
    }

    @PutMapping("/password")
    @Operation(summary = "修改管理员密码")
    public ResponseEntity<Result<Void>> changePassword(@RequestBody Map<String, String> request) {
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");

        if (oldPassword == null || oldPassword.isBlank()) {
            return Result.<Void>fail("原密码不能为空").toResponseEntity();
        }
        if (newPassword == null || newPassword.length() < 6) {
            return Result.<Void>fail("新密码长度不能少于6位").toResponseEntity();
        }
        if (!systemConfigService.getAdminPassword().equals(oldPassword)) {
            return Result.<Void>fail("原密码错误").toResponseEntity();
        }

        systemConfigService.setAdminPassword(newPassword);
        return Result.<Void>success("密码修改成功").toResponseEntity();
    }
}
