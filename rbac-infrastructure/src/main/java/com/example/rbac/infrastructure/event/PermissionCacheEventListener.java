package com.example.rbac.infrastructure.event;

import com.example.rbac.core.domain.model.user.UserRoleAssignedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 权限缓存事件监听器
 * 当用户角色或角色权限变化时，清除相关缓存
 */
@Slf4j
@Component
public class PermissionCacheEventListener {
    
    /**
     * 监听用户角色分配事件，清除用户权限缓存
     */
    public void onUserRoleAssigned(UserRoleAssignedEvent event) {
        log.info("【缓存清理】用户角色变更，清除用户权限缓存 - 用户ID: {}", event.getAggregateId());
        clearUserPermissionCache(event.getAggregateId());
    }
    
    /**
     * 监听角色权限变更事件，清除相关用户的权限缓存
     */
    public void onRolePermissionChanged(String roleId) {
        log.info("【缓存清理】角色权限变更，清除相关用户权限缓存 - 角色ID: {}", roleId);
        // TODO: 查找所有拥有该角色的用户，清除他们的权限缓存
        clearRoleRelatedCache(roleId);
    }
    
    /**
     * 清除用户权限缓存
     */
    private void clearUserPermissionCache(String userId) {
        // TODO: 实现缓存清理逻辑
        // cacheManager.evict("user_permissions", userId);
        log.debug("清除用户权限缓存: userId={}", userId);
    }
    
    /**
     * 清除角色相关缓存
     */
    private void clearRoleRelatedCache(String roleId) {
        // TODO: 实现缓存清理逻辑
        log.debug("清除角色相关缓存: roleId={}", roleId);
    }
}

