package com.example.rbac.infrastructure.persistence.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 角色JPA实体
 */
@Entity
@Table(name = "rbac_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String code;
    
    @Column(nullable = false)
    private String name;
    
    @Column
    private String description;
    
    @Column
    private String type;
    
    @Column
    private Boolean enabled;
    
    @Column
    private Long tenantId;
    
    @Column
    private LocalDateTime createdTime;
    
    @Column
    private LocalDateTime modifiedTime;
}
