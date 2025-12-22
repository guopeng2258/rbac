package com.example.rbac.core.domain.model.permission;

import com.example.rbac.core.domain.model.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 权限聚合根
 */
@Getter
@Setter
@NoArgsConstructor
public class Permission extends BaseEntity<Long> {
    
    private String code;
    
    private String name;
    
    private String description;
    
    private PermissionType type;
    
    private String resource;
    
    private String action;
    
    private Long parentId;
    
    private Integer sortOrder;
    
    private Long tenantId;
    
    /**
     * 构造方法
     */
    public Permission(String code, String name, PermissionType type, Long tenantId) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.tenantId = tenantId;
        this.createdTime = LocalDateTime.now();
    }
    
    /**
     * 检查是否是API权限
     */
    public boolean isApiPermission() {
        return PermissionType.API.equals(this.type);
    }
    
    /**
     * 检查是否是菜单权限
     */
    public boolean isMenuPermission() {
        return PermissionType.MENU.equals(this.type);
    }
}

