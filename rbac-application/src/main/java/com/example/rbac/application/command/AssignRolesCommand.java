package com.example.rbac.application.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分配角色命令
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignRolesCommand {
    private Long userId;
    private List<Long> roleIds;
}
