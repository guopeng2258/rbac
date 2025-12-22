package com.example.rbac.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 权限查询对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionQuery {
    
    /**
     * 权限编码（模糊查询）
     */
    private String code;
    
    /**
     * 权限名称（模糊查询）
     */
    private String name;
    
    /**
     * 权限类型：MENU/BUTTON/API/DATA
     */
    private String type;
    
    /**
     * 资源名称
     */
    private String resource;
    
    /**
     * 操作名称
     */
    private String action;
    
    /**
     * 父权限ID
     */
    private Long parentId;
    
    /**
     * 租户ID
     */
    private Long tenantId;
    
    /**
     * 页码
     */
    private Integer page;
    
    /**
     * 每页大小
     */
    private Integer size;
}

