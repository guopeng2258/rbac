package com.example.rbac.web.controller;

import com.example.rbac.web.config.ApiResponse;
import com.example.rbac.application.service.UserApplicationService;
import com.example.rbac.application.command.CreateUserCommand;
import com.example.rbac.application.command.AssignRolesCommand;
import com.example.rbac.application.dto.UserDTO;
import com.example.rbac.application.dto.UserPermissionsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserApplicationService userApplicationService;
    
    /**
     * 创建用户
     */
    @PostMapping
    public ApiResponse<UserDTO> createUser(@RequestBody CreateUserCommand command) {
        UserDTO result = userApplicationService.createUser(command);
        return ApiResponse.success("用户创建成功", result);
    }
    
    /**
     * 获取用户详情
     */
    @GetMapping("/{userId}")
    public ApiResponse<UserDTO> getUserDetail(@PathVariable Long userId) {
        UserDTO result = userApplicationService.getUserDetail(userId);
        return ApiResponse.success(result);
    }
    
    /**
     * 获取用户权限
     */
    @GetMapping("/{userId}/permissions")
    public ApiResponse<UserPermissionsDTO> getUserPermissions(@PathVariable Long userId) {
        UserPermissionsDTO result = userApplicationService.getUserPermissions(userId);
        return ApiResponse.success(result);
    }
    
    /**
     * 分配角色
     */
    @PutMapping("/{userId}/roles")
    public ApiResponse<UserDTO> assignRoles(@PathVariable Long userId, 
                                           @RequestBody AssignRolesCommand command) {
        command.setUserId(userId);
        UserDTO result = userApplicationService.assignRoles(command);
        return ApiResponse.success("角色分配成功", result);
    }
    
    /**
     * 锁定用户
     */
    @PostMapping("/{userId}/lock")
    public ApiResponse<UserDTO> lockUser(@PathVariable Long userId) {
        UserDTO result = userApplicationService.lockUser(userId);
        return ApiResponse.success("用户已锁定", result);
    }
    
    /**
     * 激活用户
     */
    @PostMapping("/{userId}/activate")
    public ApiResponse<UserDTO> activateUser(@PathVariable Long userId) {
        UserDTO result = userApplicationService.activateUser(userId);
        return ApiResponse.success("用户已激活", result);
    }
}
