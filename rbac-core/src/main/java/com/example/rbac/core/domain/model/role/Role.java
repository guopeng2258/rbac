package com.example.rbac.core.domain.model.role;

import com.example.rbac.core.domain.model.BaseEntity;
import com.example.rbac.core.domain.model.permission.Permission;
import com.example.rbac.core.domain.model.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 角色聚合根
 */
@Getter
@Setter
@NoArgsConstructor
public class Role extends BaseEntity<Long> {
    
    private String code;
    
    private String name;
    
    private String description;
    
    private RoleType type;
    
    private Boolean enabled = true;
    
    private Set<Permission> permissions = new HashSet<>();
    
    private Set<User> users = new HashSet<>();
    
    private Long tenantId;
    
    /**
     * 构造方法
     */
    public Role(String code, String name, RoleType type, Long tenantId) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.tenantId = tenantId;
        this.enabled = true;
        this.createdTime = LocalDateTime.now();
    }
    
    /**
     * 添加权限
     */
    public void addPermission(Permission permission) {
        if (permissions == null) {
            permissions = new HashSet<>();
        }
        permissions.add(permission);
        registerEvent(new RolePermissionAddedEvent(this.getId().toString(), permission.getId().toString()));
    }
    
    /**
     * 移除权限
     */
    public void removePermission(Permission permission) {
        if (permissions != null) {
            permissions.remove(permission);
            registerEvent(new RolePermissionRemovedEvent(this.getId().toString(), permission.getId().toString()));
        }
    }
    
    /**
     * 启用角色
     */
    public void enable() {
        this.enabled = true;
        registerEvent(new RoleEnabledEvent(this.getId().toString()));
    }
    
    /**
     * 禁用角色
     */
    public void disable() {
        this.enabled = false;
        registerEvent(new RoleDisabledEvent(this.getId().toString()));
    }
}

