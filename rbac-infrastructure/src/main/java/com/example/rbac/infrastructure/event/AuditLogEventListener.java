package com.example.rbac.infrastructure.event;

import com.example.rbac.core.domain.model.user.UserRoleAssignedEvent;
import com.example.rbac.core.domain.model.user.UserLockedEvent;
import com.example.rbac.core.domain.model.user.UserActivatedEvent;
import com.example.rbac.core.domain.model.role.RolePermissionAddedEvent;
import com.example.rbac.core.domain.model.role.RoleEnabledEvent;
import com.example.rbac.core.domain.model.role.RoleDisabledEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 审计日志事件监听器
 * 监听所有领域事件并记录审计日志
 */
@Slf4j
@Component
public class AuditLogEventListener {
    
    /**
     * 监听用户角色分配事件
     */
    public void onUserRoleAssigned(UserRoleAssignedEvent event) {
        log.info("【审计日志】用户角色分配 - 时间: {}, 用户ID: {}, 角色ID: {}", 
                LocalDateTime.now(), event.getAggregateId(), event.getRoleId());
        
        // TODO: 实际项目中应该保存到审计日志表
        saveAuditLog("USER_ROLE_ASSIGNED", 
                String.format("用户[%s]被分配角色[%s]", event.getAggregateId(), event.getRoleId()),
                event);
    }
    
    /**
     * 监听用户锁定事件
     */
    public void onUserLocked(UserLockedEvent event) {
        log.warn("【审计日志】用户锁定 - 时间: {}, 用户ID: {}", 
                LocalDateTime.now(), event.getAggregateId());
        
        saveAuditLog("USER_LOCKED", 
                String.format("用户[%s]被锁定", event.getAggregateId()),
                event);
    }
    
    /**
     * 监听用户激活事件
     */
    public void onUserActivated(UserActivatedEvent event) {
        log.info("【审计日志】用户激活 - 时间: {}, 用户ID: {}", 
                LocalDateTime.now(), event.getAggregateId());
        
        saveAuditLog("USER_ACTIVATED", 
                String.format("用户[%s]被激活", event.getAggregateId()),
                event);
    }
    
    /**
     * 监听角色权限添加事件
     */
    public void onRolePermissionAdded(RolePermissionAddedEvent event) {
        log.info("【审计日志】角色权限添加 - 时间: {}, 角色ID: {}, 权限ID: {}", 
                LocalDateTime.now(), event.getAggregateId(), event.getPermissionId());
        
        saveAuditLog("ROLE_PERMISSION_ADDED", 
                String.format("角色[%s]添加权限[%s]", event.getAggregateId(), event.getPermissionId()),
                event);
    }
    
    /**
     * 监听角色启用事件
     */
    public void onRoleEnabled(RoleEnabledEvent event) {
        log.info("【审计日志】角色启用 - 时间: {}, 角色ID: {}", 
                LocalDateTime.now(), event.getAggregateId());
        
        saveAuditLog("ROLE_ENABLED", 
                String.format("角色[%s]被启用", event.getAggregateId()),
                event);
    }
    
    /**
     * 监听角色禁用事件
     */
    public void onRoleDisabled(RoleDisabledEvent event) {
        log.warn("【审计日志】角色禁用 - 时间: {}, 角色ID: {}", 
                LocalDateTime.now(), event.getAggregateId());
        
        saveAuditLog("ROLE_DISABLED", 
                String.format("角色[%s]被禁用", event.getAggregateId()),
                event);
    }
    
    /**
     * 保存审计日志
     * 实际项目中应该保存到数据库
     */
    private void saveAuditLog(String eventType, String description, Object event) {
        // TODO: 实现保存到审计日志表的逻辑
        log.debug("保存审计日志 - 类型: {}, 描述: {}, 事件: {}", eventType, description, event);
        
        // 示例：可以保存到数据库
        // AuditLog auditLog = new AuditLog();
        // auditLog.setEventType(eventType);
        // auditLog.setDescription(description);
        // auditLog.setEventData(JSON.toJSONString(event));
        // auditLog.setCreatedTime(LocalDateTime.now());
        // auditLogRepository.save(auditLog);
    }
}

