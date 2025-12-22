package com.example.rbac.infrastructure.persistence.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 权限JPA实体
 */
@Entity
@Table(name = "rbac_permission")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionEntity {
    
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
    private String resource;
    
    @Column
    private String action;
    
    @Column
    private Long parentId;
    
    @Column
    private Integer sortOrder;
    
    @Column
    private Long tenantId;
    
    @Column
    private LocalDateTime createdTime;
    
    @Column
    private LocalDateTime modifiedTime;
}
