package com.example.rbac.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户查询对象
 * 用于封装复杂的用户查询条件
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserQuery {
    
    /**
     * 用户名（模糊查询）
     */
    private String username;
    
    /**
     * 邮箱（模糊查询）
     */
    private String email;
    
    /**
     * 用户状态
     */
    private String status;
    
    /**
     * 租户ID
     */
    private Long tenantId;
    
    /**
     * 角色编码
     */
    private String roleCode;
    
    /**
     * 是否包含角色信息
     */
    private Boolean includeRoles;
    
    /**
     * 是否包含权限信息
     */
    private Boolean includePermissions;
    
    /**
     * 页码（从0开始）
     */
    private Integer page;
    
    /**
     * 每页大小
     */
    private Integer size;
    
    /**
     * 排序字段
     */
    private String sortBy;
    
    /**
     * 排序方向：ASC/DESC
     */
    private String sortDirection;
}

