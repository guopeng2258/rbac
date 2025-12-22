package com.example.rbac.infrastructure.persistence;

import com.example.rbac.core.domain.model.permission.Permission;
import com.example.rbac.core.domain.repository.PermissionRepository;
import com.example.rbac.infrastructure.persistence.jpa.PermissionJpaRepository;
import com.example.rbac.infrastructure.persistence.mapper.PermissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限仓储实现
 */
@Repository
@RequiredArgsConstructor
public class PermissionRepositoryImpl implements PermissionRepository {
    
    private final PermissionJpaRepository permissionJpaRepository;
    private final PermissionMapper permissionMapper;
    
    @Override
    public Optional<Permission> findById(Long id) {
        return permissionJpaRepository.findById(id).map(permissionMapper::toDomain);
    }
    
    @Override
    public Optional<Permission> findByCode(String code) {
        return permissionJpaRepository.findByCode(code).map(permissionMapper::toDomain);
    }
    
    @Override
    public Optional<Permission> findByCodeAndTenantId(String code, Long tenantId) {
        return permissionJpaRepository.findByCodeAndTenantId(code, tenantId).map(permissionMapper::toDomain);
    }
    
    @Override
    public List<Permission> findByTenantId(Long tenantId) {
        return permissionJpaRepository.findByTenantId(tenantId).stream()
                .map(permissionMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public Set<String> findPermissionCodesByUserId(Long userId) {
        // 这个方法需要通过关联表查询
        // 实际实现需要添加用户-角色-权限关联查询
        return new HashSet<>();
    }
    
    @Override
    public Permission save(Permission permission) {
        var entity = permissionMapper.toEntity(permission);
        if (entity.getId() == null) {
            entity.setCreatedTime(LocalDateTime.now());
        }
        entity.setModifiedTime(LocalDateTime.now());
        var savedEntity = permissionJpaRepository.save(entity);
        return permissionMapper.toDomain(savedEntity);
    }
    
    @Override
    public void delete(Long id) {
        permissionJpaRepository.deleteById(id);
    }
}
