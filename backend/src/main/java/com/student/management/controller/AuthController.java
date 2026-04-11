package com.student.management.controller;

import com.student.management.annotation.OperationLog;
import com.student.management.service.SystemConfigService;
import com.student.management.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    @OperationLog(module = "认证管理", operationType = "登录", description = "管理员登录")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Map<String, Object> response = new HashMap<>();
        if (adminUsername.equals(username) && systemConfigService.getAdminPassword().equals(password)) {
            String token = tokenService.createToken(username);
            response.put("code", 200);
            response.put("message", "登录成功");
            response.put("token", token);
            response.put("username", username);
            return ResponseEntity.ok(response);
        }
        response.put("code", 401);
        response.put("message", "用户名或密码错误");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "管理员退出")
    @OperationLog(module = "认证管理", operationType = "退出", description = "管理员退出")
    public ResponseEntity<Map<String, Object>> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            tokenService.removeToken(authHeader.substring(7));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "退出成功");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/password")
    @Operation(summary = "修改管理员密码")
    @OperationLog(module = "认证管理", operationType = "修改密码", description = "修改管理员密码")
    public ResponseEntity<Map<String, Object>> changePassword(@RequestBody Map<String, String> request) {
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");

        Map<String, Object> response = new HashMap<>();

        if (oldPassword == null || oldPassword.isBlank()) {
            response.put("code", 400);
            response.put("message", "原密码不能为空");
            return ResponseEntity.badRequest().body(response);
        }
        if (newPassword == null || newPassword.length() < 6) {
            response.put("code", 400);
            response.put("message", "新密码长度不能少于6位");
            return ResponseEntity.badRequest().body(response);
        }
        if (!systemConfigService.getAdminPassword().equals(oldPassword)) {
            response.put("code", 400);
            response.put("message", "原密码错误");
            return ResponseEntity.badRequest().body(response);
        }

        systemConfigService.setAdminPassword(newPassword);
        response.put("code", 200);
        response.put("message", "密码修改成功");
        return ResponseEntity.ok(response);
    }
}
