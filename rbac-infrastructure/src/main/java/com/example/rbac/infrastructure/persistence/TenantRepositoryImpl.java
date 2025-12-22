package com.example.rbac.infrastructure.persistence;

import com.example.rbac.core.domain.model.tenant.Tenant;
import com.example.rbac.core.domain.repository.TenantRepository;
import com.example.rbac.infrastructure.persistence.jpa.TenantJpaRepository;
import com.example.rbac.infrastructure.persistence.mapper.TenantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 租户仓储实现
 */
@Repository
@RequiredArgsConstructor
public class TenantRepositoryImpl implements TenantRepository {
    
    private final TenantJpaRepository tenantJpaRepository;
    private final TenantMapper tenantMapper;
    
    @Override
    public Optional<Tenant> findById(Long id) {
        return tenantJpaRepository.findById(id).map(tenantMapper::toDomain);
    }
    
    @Override
    public Optional<Tenant> findByCode(String code) {
        return tenantJpaRepository.findByCode(code).map(tenantMapper::toDomain);
    }
    
    @Override
    public Tenant save(Tenant tenant) {
        var entity = tenantMapper.toEntity(tenant);
        if (entity.getId() == null) {
            entity.setCreatedTime(LocalDateTime.now());
        }
        entity.setModifiedTime(LocalDateTime.now());
        var savedEntity = tenantJpaRepository.save(entity);
        return tenantMapper.toDomain(savedEntity);
    }
    
    @Override
    public void delete(Long id) {
        tenantJpaRepository.deleteById(id);
    }
}
