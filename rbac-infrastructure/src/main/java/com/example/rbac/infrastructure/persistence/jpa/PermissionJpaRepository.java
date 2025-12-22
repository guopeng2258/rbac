package com.example.rbac.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

/**
 * 权限JPA仓储接口
 */
@Repository
public interface PermissionJpaRepository extends JpaRepository<PermissionEntity, Long> {
    
    Optional<PermissionEntity> findByCode(String code);
    
    Optional<PermissionEntity> findByCodeAndTenantId(String code, Long tenantId);
    
    List<PermissionEntity> findByTenantId(Long tenantId);
}
