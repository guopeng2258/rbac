package com.example.rbac.core.domain.model.user;

/**
 * 用户状态枚举
 */
public enum UserStatus {
    ACTIVE("活跃"),
    INACTIVE("不活跃"),
    LOCKED("锁定"),
    DELETED("删除");
    
    private final String description;
    
    UserStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}

