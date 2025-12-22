package com.example.rbac.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建用户命令
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserCommand {
    private String username;
    private String email;
    private String password;
    private String phone;
    private Long tenantId;
    private java.util.List<Long> roleIds;
}
