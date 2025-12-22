package com.example.rbac.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String status;
    private LocalDateTime lastLoginTime;
    private Long tenantId;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private Set<String> roles;
}
