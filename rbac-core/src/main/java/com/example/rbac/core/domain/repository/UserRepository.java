package com.example.rbac.core.domain.repository;

import com.example.rbac.core.domain.model.user.User;

import java.util.Optional;
import java.util.List;

/**
 * 用户仓储接口
 */
public interface UserRepository {
    
    /**
     * 根据ID查找用户
     */
    Optional<User> findById(Long id);
    
    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 根据邮箱查找用户
     */
    Optional<User> findByEmail(String email);
    
    /**
     * 根据租户ID查找所有用户
     */
    List<User> findByTenantId(Long tenantId);
    
    /**
     * 保存用户
     */
    User save(User user);
    
    /**
     * 删除用户
     */
    void delete(Long id);
}
