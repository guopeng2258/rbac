package com.example.rbac.infrastructure.persistence.mapper;

import com.example.rbac.core.domain.model.tenant.Tenant;
import com.example.rbac.infrastructure.persistence.jpa.TenantEntity;
import org.springframework.stereotype.Component;

/**
 * 租户mapper
 */
@Component
public class TenantMapper {
    
    public Tenant toDomain(TenantEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Tenant tenant = new Tenant();
        tenant.setId(entity.getId());
        tenant.setCode(entity.getCode());
        tenant.setName(entity.getName());
        tenant.setEnabled(entity.getEnabled());
        tenant.setDescription(entity.getDescription());
        tenant.setMaxUsers(entity.getMaxUsers());
        tenant.setCreatedTime(entity.getCreatedTime());
        tenant.setModifiedTime(entity.getModifiedTime());
        
        return tenant;
    }
    
    public TenantEntity toEntity(Tenant domain) {
        if (domain == null) {
            return null;
        }
        
        TenantEntity entity = new TenantEntity();
        entity.setId(domain.getId());
        entity.setCode(domain.getCode());
        entity.setName(domain.getName());
        entity.setEnabled(domain.getEnabled());
        entity.setDescription(domain.getDescription());
        entity.setMaxUsers(domain.getMaxUsers());
        entity.setCreatedTime(domain.getCreatedTime());
        entity.setModifiedTime(domain.getModifiedTime());
        
        return entity;
    }
}
