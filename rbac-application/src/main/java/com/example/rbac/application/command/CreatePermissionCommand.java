package com.example.rbac.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建权限命令
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePermissionCommand {
    private String code;
    private String name;
    private String description;
    private String type;
    private String resource;
    private String action;
    private Long parentId;
    private Integer sortOrder;
    private Long tenantId;
}
