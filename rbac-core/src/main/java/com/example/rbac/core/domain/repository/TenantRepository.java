package com.example.rbac.core.domain.repository;

import com.example.rbac.core.domain.model.tenant.Tenant;

import java.util.Optional;

/**
 * 租户仓储接口
 */
public interface TenantRepository {
    
    /**
     * 根据ID查找租户
     */
    Optional<Tenant> findById(Long id);
    
    /**
     * 根据租户编码查找租户
     */
    Optional<Tenant> findByCode(String code);
    
    /**
     * 保存租户
     */
    Tenant save(Tenant tenant);
    
    /**
     * 删除租户
     */
    void delete(Long id);
}
