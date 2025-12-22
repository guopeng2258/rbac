package com.example.rbac.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 角色DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String type;
    private Boolean enabled;
    private Long tenantId;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private Set<String> permissions;
}
