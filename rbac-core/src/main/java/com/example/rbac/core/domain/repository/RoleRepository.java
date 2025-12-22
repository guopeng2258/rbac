package com.example.rbac.core.domain.repository;

import com.example.rbac.core.domain.model.role.Role;

import java.util.Optional;
import java.util.List;

/**
 * 角色仓储接口
 */
public interface RoleRepository {
    
    /**
     * 根据ID查找角色
     */
    Optional<Role> findById(Long id);
    
    /**
     * 根据角色编码查找角色
     */
    Optional<Role> findByCode(String code);
    
    /**
     * 根据角色编码和租户ID查找角色
     */
    Optional<Role> findByCodeAndTenantId(String code, Long tenantId);
    
    /**
     * 根据用户ID查找用户的角色
     */
    List<Role> findByUserId(Long userId);
    
    /**
     * 根据租户ID查找所有角色
     */
    List<Role> findByTenantId(Long tenantId);
    
    /**
     * 保存角色
     */
    Role save(Role role);
    
    /**
     * 删除角色
     */
    void delete(Long id);
}
