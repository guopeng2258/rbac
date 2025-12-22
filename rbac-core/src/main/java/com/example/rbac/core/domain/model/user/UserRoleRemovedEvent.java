package com.example.rbac.core.domain.model.user;

import com.example.rbac.core.domain.model.DomainEvent;

/**
 * 用户角色移除事件
 */
public class UserRoleRemovedEvent extends DomainEvent {
    
    private final String roleId;
    
    public UserRoleRemovedEvent(String userId, String roleId) {
        super(userId);
        this.roleId = roleId;
    }
    
    public String getRoleId() {
        return roleId;
    }
}

