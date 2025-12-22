package com.example.rbac.core.domain.model.role;

import com.example.rbac.core.domain.model.DomainEvent;

/**
 * 角色权限添加事件
 */
public class RolePermissionAddedEvent extends DomainEvent {
    
    private final String permissionId;
    
    public RolePermissionAddedEvent(String roleId, String permissionId) {
        super(roleId);
        this.permissionId = permissionId;
    }
    
    public String getPermissionId() {
        return permissionId;
    }
}

