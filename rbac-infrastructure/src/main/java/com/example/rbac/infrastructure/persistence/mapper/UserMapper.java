package com.example.rbac.infrastructure.persistence.mapper;

import com.example.rbac.core.domain.model.user.User;
import com.example.rbac.core.domain.model.user.UserStatus;
import com.example.rbac.infrastructure.persistence.jpa.UserEntity;
import org.springframework.stereotype.Component;

/**
 * 用户mapper
 */
@Component
public class UserMapper {
    
    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        
        User user = new User();
        user.setId(entity.getId());
        user.setUsername(entity.getUsername());
        user.setEmail(entity.getEmail());
        user.setPassword(entity.getPassword());
        user.setPhone(entity.getPhone());
        user.setStatus(entity.getStatus() != null ? UserStatus.valueOf(entity.getStatus()) : UserStatus.ACTIVE);
        user.setLastLoginTime(entity.getLastLoginTime());
        user.setTenantId(entity.getTenantId());
        user.setCreatedTime(entity.getCreatedTime());
        user.setModifiedTime(entity.getModifiedTime());
        
        return user;
    }
    
    public UserEntity toEntity(User domain) {
        if (domain == null) {
            return null;
        }
        
        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setUsername(domain.getUsername());
        entity.setEmail(domain.getEmail());
        entity.setPassword(domain.getPassword());
        entity.setPhone(domain.getPhone());
        entity.setStatus(domain.getStatus() != null ? domain.getStatus().name() : UserStatus.ACTIVE.name());
        entity.setLastLoginTime(domain.getLastLoginTime());
        entity.setTenantId(domain.getTenantId());
        entity.setCreatedTime(domain.getCreatedTime());
        entity.setModifiedTime(domain.getModifiedTime());
        
        return entity;
    }
}
