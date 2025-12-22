package com.example.rbac.web.controller;

import com.example.rbac.web.config.ApiResponse;
import com.example.rbac.application.service.PermissionApplicationService;
import com.example.rbac.application.command.CreatePermissionCommand;
import com.example.rbac.application.dto.PermissionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限控制器
 */
@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {
    
    private final PermissionApplicationService permissionApplicationService;
    
    /**
     * 创建权限
     */
    @PostMapping
    public ApiResponse<PermissionDTO> createPermission(@RequestBody CreatePermissionCommand command) {
        PermissionDTO result = permissionApplicationService.createPermission(command);
        return ApiResponse.success("权限创建成功", result);
    }
    
    /**
     * 获取权限详情
     */
    @GetMapping("/{permissionId}")
    public ApiResponse<PermissionDTO> getPermissionDetail(@PathVariable Long permissionId) {
        PermissionDTO result = permissionApplicationService.getPermissionDetail(permissionId);
        return ApiResponse.success(result);
    }
    
    /**
     * 查询租户的所有权限
     */
    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<List<PermissionDTO>> listPermissions(@PathVariable Long tenantId) {
        List<PermissionDTO> result = permissionApplicationService.listPermissions(tenantId);
        return ApiResponse.success(result);
    }
    
    /**
     * 删除权限
     */
    @DeleteMapping("/{permissionId}")
    public ApiResponse<String> deletePermission(@PathVariable Long permissionId) {
        permissionApplicationService.deletePermission(permissionId);
        return ApiResponse.success("权限已删除");
    }
}
