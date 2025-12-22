package com.example.rbac.application.assembler;

import com.example.rbac.core.domain.model.role.Role;
import com.example.rbac.application.dto.RoleDTO;

import java.util.stream.Collectors;

/**
 * 角色数据组装器
 */
public class RoleAssembler {
    
    public static RoleDTO toDTO(Role role) {
        if (role == null) {
            return null;
        }
        
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setCode(role.getCode());
        dto.setName(role.getName());
        dto.setDescription(role.getDescription());
        dto.setType(role.getType() != null ? role.getType().name() : null);
        dto.setEnabled(role.getEnabled());
        dto.setTenantId(role.getTenantId());
        dto.setCreatedTime(role.getCreatedTime());
        dto.setModifiedTime(role.getModifiedTime());
        
        if (role.getPermissions() != null) {
            dto.setPermissions(role.getPermissions().stream()
                    .map(permission -> permission.getCode())
                    .collect(Collectors.toSet()));
        }
        
        return dto;
    }
}

