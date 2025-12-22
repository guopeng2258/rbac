package com.example.rbac.core.domain.model.tenant;

import com.example.rbac.core.domain.model.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 租户聚合根
 */
@Getter
@Setter
@NoArgsConstructor
public class Tenant extends BaseEntity<Long> {
    
    private String code;
    
    private String name;
    
    private Boolean enabled = true;
    
    private String description;
    
    private Integer maxUsers;
    
    /**
     * 构造方法
     */
    public Tenant(String code, String name) {
        this.code = code;
        this.name = name;
        this.enabled = true;
        this.createdTime = LocalDateTime.now();
    }
    
    /**
     * 启用租户
     */
    public void enable() {
        this.enabled = true;
    }
    
    /**
     * 禁用租户
     */
    public void disable() {
        this.enabled = false;
    }
}

