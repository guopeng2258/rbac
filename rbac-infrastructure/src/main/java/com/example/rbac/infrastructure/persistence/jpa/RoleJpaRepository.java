package com.example.rbac.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

/**
 * 角色JPA仓储接口
 */
@Repository
public interface RoleJpaRepository extends JpaRepository<RoleEntity, Long> {
    
    Optional<RoleEntity> findByCode(String code);
    
    Optional<RoleEntity> findByCodeAndTenantId(String code, Long tenantId);
    
    List<RoleEntity> findByTenantId(Long tenantId);
}
