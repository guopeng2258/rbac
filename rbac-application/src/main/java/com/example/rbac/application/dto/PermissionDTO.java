package com.example.rbac.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 权限DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String type;
    private String resource;
    private String action;
    private Long parentId;
    private Integer sortOrder;
    private Long tenantId;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
}
