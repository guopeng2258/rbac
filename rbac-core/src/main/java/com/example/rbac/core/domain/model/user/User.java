package com.example.rbac.core.domain.model.user;

import com.example.rbac.core.domain.model.BaseEntity;
import com.example.rbac.core.domain.model.role.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户聚合根
 */
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity<Long> {
    
    private String username;
    
    private String email;
    
    private String password;
    
    private String phone;
    
    private UserStatus status;
    
    private LocalDateTime lastLoginTime;
    
    private Set<Role> roles = new HashSet<>();
    
    private Long tenantId;
    
    /**
     * 构造方法
     */
    public User(String username, String email, String password, Long tenantId) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.tenantId = tenantId;
        this.status = UserStatus.ACTIVE;
        this.createdTime = LocalDateTime.now();
    }
    
    /**
     * 分配角色
     */
    public void assignRole(Role role) {
        if (roles == null) {
            roles = new HashSet<>();
        }
        roles.add(role);
        registerEvent(new UserRoleAssignedEvent(this.getId().toString(), role.getId().toString()));
    }
    
    /**
     * 移除角色
     */
    public void removeRole(Role role) {
        if (roles != null) {
            roles.remove(role);
            registerEvent(new UserRoleRemovedEvent(this.getId().toString(), role.getId().toString()));
        }
    }
    
    /**
     * 检查是否拥有权限
     */
    public boolean hasPermission(String permissionCode) {
        return roles.stream()
                .filter(role -> role.getEnabled() != null && role.getEnabled())
                .flatMap(role -> role.getPermissions().stream())
                .anyMatch(permission -> permission.getCode().equals(permissionCode));
    }
    
    /**
     * 检查是否拥有角色
     */
    public boolean hasRole(String roleCode) {
        return roles.stream()
                .anyMatch(role -> role.getCode().equals(roleCode) && (role.getEnabled() == null || role.getEnabled()));
    }
    
    /**
     * 锁定用户
     */
    public void lock() {
        this.status = UserStatus.LOCKED;
        registerEvent(new UserLockedEvent(this.getId().toString()));
    }
    
    /**
     * 激活用户
     */
    public void activate() {
        this.status = UserStatus.ACTIVE;
        registerEvent(new UserActivatedEvent(this.getId().toString()));
    }
    
    /**
     * 更新最后登录时间
     */
    public void updateLastLoginTime() {
        this.lastLoginTime = LocalDateTime.now();
    }
}

