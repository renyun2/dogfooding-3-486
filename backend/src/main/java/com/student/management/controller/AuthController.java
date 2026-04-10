package com.student.management.controller;

import com.student.management.common.Result;
import com.student.management.service.SystemConfigService;
import com.student.management.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
            Map<String, Object> data = Map.of(
                "token", token,
                "username", username
            );
            return ResponseEntity.ok(Result.success("登录成功", data));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Result.fail(401, "用户名或密码错误"));
    }

    @PostMapping("/logout")
    @Operation(summary = "管理员退出")
    public ResponseEntity<Result<Void>> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            tokenService.removeToken(authHeader.substring(7));
        }
        return ResponseEntity.ok(Result.success("退出成功"));
    }

    @PutMapping("/password")
    @Operation(summary = "修改管理员密码")
    public ResponseEntity<Result<Void>> changePassword(@RequestBody Map<String, String> request) {
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");

        if (oldPassword == null || oldPassword.isBlank()) {
            return ResponseEntity.badRequest().body(Result.fail(400, "原密码不能为空"));
        }
        if (newPassword == null || newPassword.length() < 6) {
            return ResponseEntity.badRequest().body(Result.fail(400, "新密码长度不能少于6位"));
        }
        if (!systemConfigService.getAdminPassword().equals(oldPassword)) {
            return ResponseEntity.badRequest().body(Result.fail(400, "原密码错误"));
        }

        systemConfigService.setAdminPassword(newPassword);
        return ResponseEntity.ok(Result.success("密码修改成功"));
    }
}
