package com.example.rbac.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 创建角色命令
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRoleCommand {
    private String code;
    private String name;
    private String description;
    private String type;
    private Long tenantId;
    private List<Long> permissionIds;
}
