package com.example.rbac.infrastructure.persistence.mapper;

import com.example.rbac.core.domain.model.permission.Permission;
import com.example.rbac.core.domain.model.permission.PermissionType;
import com.example.rbac.infrastructure.persistence.jpa.PermissionEntity;
import org.springframework.stereotype.Component;

/**
 * 权限mapper
 */
@Component
public class PermissionMapper {
    
    public Permission toDomain(PermissionEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Permission permission = new Permission();
        permission.setId(entity.getId());
        permission.setCode(entity.getCode());
        permission.setName(entity.getName());
        permission.setDescription(entity.getDescription());
        permission.setType(entity.getType() != null ? PermissionType.valueOf(entity.getType()) : null);
        permission.setResource(entity.getResource());
        permission.setAction(entity.getAction());
        permission.setParentId(entity.getParentId());
        permission.setSortOrder(entity.getSortOrder());
        permission.setTenantId(entity.getTenantId());
        permission.setCreatedTime(entity.getCreatedTime());
        permission.setModifiedTime(entity.getModifiedTime());
        
        return permission;
    }
    
    public PermissionEntity toEntity(Permission domain) {
        if (domain == null) {
            return null;
        }
        
        PermissionEntity entity = new PermissionEntity();
        entity.setId(domain.getId());
        entity.setCode(domain.getCode());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setType(domain.getType() != null ? domain.getType().name() : null);
        entity.setResource(domain.getResource());
        entity.setAction(domain.getAction());
        entity.setParentId(domain.getParentId());
        entity.setSortOrder(domain.getSortOrder());
        entity.setTenantId(domain.getTenantId());
        entity.setCreatedTime(domain.getCreatedTime());
        entity.setModifiedTime(domain.getModifiedTime());
        
        return entity;
    }
}
