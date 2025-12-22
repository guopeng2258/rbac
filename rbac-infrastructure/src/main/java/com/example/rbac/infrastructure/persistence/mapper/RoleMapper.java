package com.example.rbac.infrastructure.persistence.mapper;

import com.example.rbac.core.domain.model.role.Role;
import com.example.rbac.core.domain.model.role.RoleType;
import com.example.rbac.infrastructure.persistence.jpa.RoleEntity;
import org.springframework.stereotype.Component;

/**
 * 角色mapper
 */
@Component
public class RoleMapper {
    
    public Role toDomain(RoleEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Role role = new Role();
        role.setId(entity.getId());
        role.setCode(entity.getCode());
        role.setName(entity.getName());
        role.setDescription(entity.getDescription());
        role.setType(entity.getType() != null ? RoleType.valueOf(entity.getType()) : null);
        role.setEnabled(entity.getEnabled());
        role.setTenantId(entity.getTenantId());
        role.setCreatedTime(entity.getCreatedTime());
        role.setModifiedTime(entity.getModifiedTime());
        
        return role;
    }
    
    public RoleEntity toEntity(Role domain) {
        if (domain == null) {
            return null;
        }
        
        RoleEntity entity = new RoleEntity();
        entity.setId(domain.getId());
        entity.setCode(domain.getCode());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setType(domain.getType() != null ? domain.getType().name() : null);
        entity.setEnabled(domain.getEnabled());
        entity.setTenantId(domain.getTenantId());
        entity.setCreatedTime(domain.getCreatedTime());
        entity.setModifiedTime(domain.getModifiedTime());
        
        return entity;
    }
}
