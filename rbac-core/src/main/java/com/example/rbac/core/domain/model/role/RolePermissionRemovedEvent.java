package com.example.rbac.core.domain.model.role;

import com.example.rbac.core.domain.model.DomainEvent;

/**
 * 角色权限移除事件
 */
public class RolePermissionRemovedEvent extends DomainEvent {
    
    private final String permissionId;
    
    public RolePermissionRemovedEvent(String roleId, String permissionId) {
        super(roleId);
        this.permissionId = permissionId;
    }
    
    public String getPermissionId() {
        return permissionId;
    }
}

