package com.example.rbac.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色查询对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleQuery {
    
    /**
     * 角色编码
     */
    private String code;
    
    /**
     * 角色名称（模糊查询）
     */
    private String name;
    
    /**
     * 角色类型：SYSTEM/CUSTOM
     */
    private String type;
    
    /**
     * 是否启用
     */
    private Boolean enabled;
    
    /**
     * 租户ID
     */
    private Long tenantId;
    
    /**
     * 是否包含权限信息
     */
    private Boolean includePermissions;
    
    /**
     * 页码
     */
    private Integer page;
    
    /**
     * 每页大小
     */
    private Integer size;
}

