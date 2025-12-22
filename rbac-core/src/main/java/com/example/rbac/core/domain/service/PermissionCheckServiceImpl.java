package com.example.rbac.core.domain.service;

import com.example.rbac.core.domain.model.permission.Permission;
import com.example.rbac.core.domain.model.role.Role;
import com.example.rbac.core.domain.model.user.User;

import java.util.HashSet;
import java.util.Set;

/**
 * 权限校验领域服务实现
 */
public class PermissionCheckServiceImpl implements PermissionCheckService {

    @Override
    public boolean checkPermission(User user, String resource, String action) {
        if (user == null || user.getRoles() == null || user.getRoles().isEmpty()) {
            return false;
        }
        
        return user.getRoles().stream()
                .filter(role -> role.getEnabled() != null && role.getEnabled())
                .flatMap(role -> role.getPermissions().stream())
                .anyMatch(permission -> 
                    resource.equals(permission.getResource()) &&
                    action.equals(permission.getAction()));
    }

    @Override
    public boolean checkRole(User user, String roleCode) {
        if (user == null || user.getRoles() == null || user.getRoles().isEmpty()) {
            return false;
        }
        
        return user.getRoles().stream()
                .filter(role -> role.getEnabled() != null && role.getEnabled())
                .anyMatch(role -> roleCode.equals(role.getCode()));
    }

    @Override
    public boolean hasPermission(Role role, String permissionCode) {
        if (role == null || role.getPermissions() == null || role.getPermissions().isEmpty()) {
            return false;
        }
        
        return role.getPermissions().stream()
                .anyMatch(permission -> permissionCode.equals(permission.getCode()));
    }

    @Override
    public Set<String> getUserPermissions(User user) {
        Set<String> permissions = new HashSet<>();
        
        if (user == null || user.getRoles() == null) {
            return permissions;
        }
        
        user.getRoles().stream()
                .filter(role -> role.getEnabled() != null && role.getEnabled())
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getCode)
                .forEach(permissions::add);
        
        return permissions;
    }
}

