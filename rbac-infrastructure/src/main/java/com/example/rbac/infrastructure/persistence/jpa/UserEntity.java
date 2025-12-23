package com.example.rbac.infrastructure.persistence.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户JPA实体
 */
@Entity
@Table(name = "rbac_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column
    private String email;
    
    @Column
    private String password;
    
    @Column
    private String phone;
    
    @Column
    private String status;
    
    @Column
    private LocalDateTime lastLoginTime;
    
    @Column
    private Long tenantId;
    
    @Column
    private LocalDateTime createdTime;
    
    @Column
    private LocalDateTime modifiedTime;
}
