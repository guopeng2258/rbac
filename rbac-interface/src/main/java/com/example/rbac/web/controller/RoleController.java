package com.example.rbac.web.controller;

import com.example.rbac.web.config.ApiResponse;
import com.example.rbac.application.service.RoleApplicationService;
import com.example.rbac.application.command.CreateRoleCommand;
import com.example.rbac.application.dto.RoleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色控制器
 */
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    
    private final RoleApplicationService roleApplicationService;
    
    /**
     * 创建角色
     */
    @PostMapping
    public ApiResponse<RoleDTO> createRole(@RequestBody CreateRoleCommand command) {
        RoleDTO result = roleApplicationService.createRole(command);
        return ApiResponse.success("角色创建成功", result);
    }
    
    /**
     * 获取角色详情
     */
    @GetMapping("/{roleId}")
    public ApiResponse<RoleDTO> getRoleDetail(@PathVariable Long roleId) {
        RoleDTO result = roleApplicationService.getRoleDetail(roleId);
        return ApiResponse.success(result);
    }
    
    /**
     * 给角色添加权限
     */
    @PostMapping("/{roleId}/permissions/{permissionId}")
    public ApiResponse<RoleDTO> addPermissionToRole(@PathVariable Long roleId, 
                                                   @PathVariable Long permissionId) {
        RoleDTO result = roleApplicationService.addPermissionToRole(roleId, permissionId);
        return ApiResponse.success("权限添加成功", result);
    }
    
    /**
     * 从角色移除权限
     */
    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    public ApiResponse<RoleDTO> removePermissionFromRole(@PathVariable Long roleId, 
                                                        @PathVariable Long permissionId) {
        RoleDTO result = roleApplicationService.removePermissionFromRole(roleId, permissionId);
        return ApiResponse.success("权限移除成功", result);
    }
    
    /**
     * 启用角色
     */
    @PostMapping("/{roleId}/enable")
    public ApiResponse<RoleDTO> enableRole(@PathVariable Long roleId) {
        RoleDTO result = roleApplicationService.enableRole(roleId);
        return ApiResponse.success("角色已启用", result);
    }
    
    /**
     * 禁用角色
     */
    @PostMapping("/{roleId}/disable")
    public ApiResponse<RoleDTO> disableRole(@PathVariable Long roleId) {
        RoleDTO result = roleApplicationService.disableRole(roleId);
        return ApiResponse.success("角色已禁用", result);
    }
    
    /**
     * 获取租户的所有角色
     */
    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<List<RoleDTO>> listRolesByTenant(@PathVariable Long tenantId) {
        List<RoleDTO> result = roleApplicationService.listRolesByTenant(tenantId);
        return ApiResponse.success(result);
    }
}
