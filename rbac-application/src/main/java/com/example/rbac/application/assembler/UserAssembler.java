package com.example.rbac.application.assembler;

import com.example.rbac.core.domain.model.user.User;
import com.example.rbac.application.dto.UserDTO;
import com.example.rbac.application.dto.UserPermissionsDTO;

import java.util.stream.Collectors;

/**
 * 用户数据组装器
 */
public class UserAssembler {
    
    public static UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setStatus(user.getStatus() != null ? user.getStatus().name() : null);
        dto.setLastLoginTime(user.getLastLoginTime());
        dto.setTenantId(user.getTenantId());
        dto.setCreatedTime(user.getCreatedTime());
        dto.setModifiedTime(user.getModifiedTime());
        
        if (user.getRoles() != null) {
            dto.setRoles(user.getRoles().stream()
                    .map(role -> role.getCode())
                    .collect(Collectors.toSet()));
        }
        
        return dto;
    }
    
    public static UserPermissionsDTO toPermissionDTO(User user) {
        if (user == null) {
            return null;
        }
        
        UserPermissionsDTO dto = new UserPermissionsDTO();
        dto.setUserId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setTenantId(user.getTenantId());
        
        // 获取所有角色
        if (user.getRoles() != null) {
            dto.setRoles(user.getRoles().stream()
                    .map(role -> role.getCode())
                    .collect(Collectors.toSet()));
        }
        
        // 获取所有权限
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            dto.setPermissions(user.getRoles().stream()
                    .filter(role -> role.getEnabled() != null && role.getEnabled())
                    .flatMap(role -> role.getPermissions().stream())
                    .map(permission -> permission.getCode())
                    .collect(Collectors.toSet()));
        }
        
        return dto;
    }
}

