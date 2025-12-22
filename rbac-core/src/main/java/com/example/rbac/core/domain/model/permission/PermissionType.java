package com.example.rbac.core.domain.model.permission;

/**
 * 权限类型枚举
 */
public enum PermissionType {
    MENU("菜单"),
    BUTTON("按钮"),
    API("API"),
    DATA("数据");
    
    private final String description;
    
    PermissionType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}

