package com.example.rbac.application.assembler;

import com.example.rbac.core.domain.model.permission.Permission;
import com.example.rbac.application.dto.PermissionDTO;

/**
 * 权限数据组装器
 */
public class PermissionAssembler {
    
    public static PermissionDTO toDTO(Permission permission) {
        if (permission == null) {
            return null;
        }
        
        PermissionDTO dto = new PermissionDTO();
        dto.setId(permission.getId());
        dto.setCode(permission.getCode());
        dto.setName(permission.getName());
        dto.setDescription(permission.getDescription());
        dto.setType(permission.getType() != null ? permission.getType().name() : null);
        dto.setResource(permission.getResource());
        dto.setAction(permission.getAction());
        dto.setParentId(permission.getParentId());
        dto.setSortOrder(permission.getSortOrder());
        dto.setTenantId(permission.getTenantId());
        dto.setCreatedTime(permission.getCreatedTime());
        dto.setModifiedTime(permission.getModifiedTime());
        
        return dto;
    }
}

