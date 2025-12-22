package com.example.rbac.core.domain.repository;

import com.example.rbac.core.domain.model.permission.Permission;

import java.util.Optional;
import java.util.List;
import java.util.Set;

/**
 * 权限仓储接口
 */
public interface PermissionRepository {
    
    /**
     * 根据ID查找权限
     */
    Optional<Permission> findById(Long id);
    
    /**
     * 根据权限编码查找权限
     */
    Optional<Permission> findByCode(String code);
    
    /**
     * 根据权限编码和租户ID查找权限
     */
    Optional<Permission> findByCodeAndTenantId(String code, Long tenantId);
    
    /**
     * 根据租户ID查找所有权限
     */
    List<Permission> findByTenantId(Long tenantId);
    
    /**
     * 根据用户ID查找用户的权限编码
     */
    Set<String> findPermissionCodesByUserId(Long userId);
    
    /**
     * 保存权限
     */
    Permission save(Permission permission);
    
    /**
     * 删除权限
     */
    void delete(Long id);
}
