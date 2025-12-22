package com.example.rbac.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 用户权限DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPermissionsDTO {
    private Long userId;
    private String username;
    private Long tenantId;
    private Set<String> roles;
    private Set<String> permissions;
}
