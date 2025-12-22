package com.example.rbac.web.controller;

import com.example.rbac.web.config.ApiResponse;
import com.example.rbac.application.query.UserQuery;
import com.example.rbac.application.query.UserQueryService;
import com.example.rbac.application.dto.UserDTO;
import com.example.rbac.application.dto.UserPermissionsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户查询控制器
 * CQRS模式 - 查询端API
 */
@RestController
@RequestMapping("/api/query/users")
@RequiredArgsConstructor
public class UserQueryController {
    
    private final UserQueryService userQueryService;
    
    /**
     * 复杂条件查询用户
     */
    @PostMapping("/search")
    public ApiResponse<List<UserDTO>> searchUsers(@RequestBody UserQuery query) {
        List<UserDTO> users = userQueryService.queryUsers(query);
        return ApiResponse.success(users);
    }
    
    /**
     * 根据用户名查询
     */
    @GetMapping("/by-username/{username}")
    public ApiResponse<UserDTO> getUserByUsername(@PathVariable String username) {
        UserDTO user = userQueryService.queryByUsername(username);
        return ApiResponse.success(user);
    }
    
    /**
     * 查询用户完整权限信息
     */
    @GetMapping("/{userId}/full-permissions")
    public ApiResponse<UserPermissionsDTO> getUserFullPermissions(@PathVariable Long userId) {
        UserPermissionsDTO permissions = userQueryService.queryUserPermissions(userId);
        return ApiResponse.success(permissions);
    }
    
    /**
     * 统计租户用户数
     */
    @GetMapping("/count/tenant/{tenantId}")
    public ApiResponse<Long> countUsersByTenant(@PathVariable Long tenantId) {
        long count = userQueryService.countUsersByTenant(tenantId);
        return ApiResponse.success(count);
    }
    
    /**
     * 检查用户名是否存在
     */
    @GetMapping("/exists/{username}")
    public ApiResponse<Boolean> checkUsernameExists(@PathVariable String username) {
        boolean exists = userQueryService.existsByUsername(username);
        return ApiResponse.success(exists);
    }
}

