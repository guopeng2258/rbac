package com.example.rbac.core.domain.model.user;

import com.example.rbac.core.domain.model.DomainEvent;

/**
 * 用户角色分配事件
 */
public class UserRoleAssignedEvent extends DomainEvent {
    
    private final String roleId;
    
    public UserRoleAssignedEvent(String userId, String roleId) {
        super(userId);
        this.roleId = roleId;
    }
    
    public String getRoleId() {
        return roleId;
    }
}

