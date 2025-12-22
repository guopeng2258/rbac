package com.example.rbac.infrastructure.event;

import com.example.rbac.core.domain.model.DomainEvent;
import com.example.rbac.core.domain.service.DomainEventPublisher;
import com.example.rbac.core.domain.model.user.*;
import com.example.rbac.core.domain.model.role.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 领域事件发布实现（增强版）
 * 支持事件监听和分发
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DomainEventPublisherImpl implements DomainEventPublisher {
    
    private final AuditLogEventListener auditLogEventListener;
    private final PermissionCacheEventListener permissionCacheEventListener;
    private final NotificationEventListener notificationEventListener;
    
    @Override
    public void publish(DomainEvent event) {
        if (event == null) {
            return;
        }
        
        log.info("发布领域事件: {}", event.getClass().getSimpleName());
        
        // 分发到不同的监听器
        dispatchEvent(event);
    }
    
    /**
     * 分发事件到各个监听器
     */
    private void dispatchEvent(DomainEvent event) {
        try {
            // 用户相关事件
            if (event instanceof UserRoleAssignedEvent) {
                UserRoleAssignedEvent e = (UserRoleAssignedEvent) event;
                auditLogEventListener.onUserRoleAssigned(e);
                permissionCacheEventListener.onUserRoleAssigned(e);
            } 
            else if (event instanceof UserLockedEvent) {
                UserLockedEvent e = (UserLockedEvent) event;
                auditLogEventListener.onUserLocked(e);
                notificationEventListener.onUserLocked(e);
            } 
            else if (event instanceof UserActivatedEvent) {
                UserActivatedEvent e = (UserActivatedEvent) event;
                auditLogEventListener.onUserActivated(e);
            }
            // 角色相关事件
            else if (event instanceof RolePermissionAddedEvent) {
                RolePermissionAddedEvent e = (RolePermissionAddedEvent) event;
                auditLogEventListener.onRolePermissionAdded(e);
                permissionCacheEventListener.onRolePermissionChanged(e.getAggregateId());
            }
            else if (event instanceof RoleEnabledEvent) {
                RoleEnabledEvent e = (RoleEnabledEvent) event;
                auditLogEventListener.onRoleEnabled(e);
            }
            else if (event instanceof RoleDisabledEvent) {
                RoleDisabledEvent e = (RoleDisabledEvent) event;
                auditLogEventListener.onRoleDisabled(e);
            }
            
        } catch (Exception e) {
            log.error("事件处理失败: {}", event.getClass().getSimpleName(), e);
            // 事件处理失败不应该影响主流程
        }
    }
}
