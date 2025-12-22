package com.example.rbac.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 租户JPA仓储接口
 */
@Repository
public interface TenantJpaRepository extends JpaRepository<TenantEntity, Long> {
    
    Optional<TenantEntity> findByCode(String code);
}
