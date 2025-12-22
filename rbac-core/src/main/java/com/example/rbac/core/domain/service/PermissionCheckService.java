package com.example.rbac.core.domain.service;

import com.example.rbac.core.domain.model.permission.Permission;
import com.example.rbac.core.domain.model.role.Role;
import com.example.rbac.core.domain.model.user.User;

/**
 * 权限校验领域服务
 */
public interface PermissionCheckService {
    
    /**
     * 检查用户是否拥有指定权限
     */
    boolean checkPermission(User user, String resource, String action);
    
    /**
     * 检查用户是否拥有指定角色
     */
    boolean checkRole(User user, String roleCode);
    
    /**
     * 检查角色是否拥有指定权限
     */
    boolean hasPermission(Role role, String permissionCode);
    
    /**
     * 获取用户的所有权限编码
     */
    java.util.Set<String> getUserPermissions(User user);
}

