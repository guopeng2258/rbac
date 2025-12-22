package com.example.rbac.infrastructure.persistence;

import com.example.rbac.core.domain.model.role.Role;
import com.example.rbac.core.domain.repository.RoleRepository;
import com.example.rbac.infrastructure.persistence.jpa.RoleJpaRepository;
import com.example.rbac.infrastructure.persistence.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 角色仓储实现
 */
@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {
    
    private final RoleJpaRepository roleJpaRepository;
    private final RoleMapper roleMapper;
    
    @Override
    public Optional<Role> findById(Long id) {
        return roleJpaRepository.findById(id).map(roleMapper::toDomain);
    }
    
    @Override
    public Optional<Role> findByCode(String code) {
        return roleJpaRepository.findByCode(code).map(roleMapper::toDomain);
    }
    
    @Override
    public Optional<Role> findByCodeAndTenantId(String code, Long tenantId) {
        return roleJpaRepository.findByCodeAndTenantId(code, tenantId).map(roleMapper::toDomain);
    }
    
    @Override
    public List<Role> findByUserId(Long userId) {
        // 这个方法需要通过关联表查询，暂时返回空列表
        // 实际实现需要添加用户-角色关联表
        return List.of();
    }
    
    @Override
    public List<Role> findByTenantId(Long tenantId) {
        return roleJpaRepository.findByTenantId(tenantId).stream()
                .map(roleMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public Role save(Role role) {
        var entity = roleMapper.toEntity(role);
        if (entity.getId() == null) {
            entity.setCreatedTime(LocalDateTime.now());
        }
        entity.setModifiedTime(LocalDateTime.now());
        var savedEntity = roleJpaRepository.save(entity);
        return roleMapper.toDomain(savedEntity);
    }
    
    @Override
    public void delete(Long id) {
        roleJpaRepository.deleteById(id);
    }
}
