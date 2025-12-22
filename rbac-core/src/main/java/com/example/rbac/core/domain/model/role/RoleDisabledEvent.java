package com.example.rbac.core.domain.model.role;

import com.example.rbac.core.domain.model.DomainEvent;

/**
 * 角色禁用事件
 */
public class RoleDisabledEvent extends DomainEvent {
    
    public RoleDisabledEvent(String roleId) {
        super(roleId);
    }
}

