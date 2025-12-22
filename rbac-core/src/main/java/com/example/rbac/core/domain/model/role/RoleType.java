package com.example.rbac.core.domain.model.role;

/**
 * 角色类型枚举
 */
public enum RoleType {
    SYSTEM("系统角色"),
    CUSTOM("自定义角色");
    
    private final String description;
    
    RoleType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}

