package com.example.rbac.core.domain.model.role;

import com.example.rbac.core.domain.model.DomainEvent;

/**
 * 角色启用事件
 */
public class RoleEnabledEvent extends DomainEvent {
    
    public RoleEnabledEvent(String roleId) {
        super(roleId);
    }
}

