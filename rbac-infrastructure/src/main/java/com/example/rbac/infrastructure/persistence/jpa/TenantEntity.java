package com.example.rbac.infrastructure.persistence.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 租户JPA实体
 */
@Entity
@Table(name = "rbac_tenant")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String code;
    
    @Column(nullable = false)
    private String name;
    
    @Column
    private Boolean enabled;
    
    @Column
    private String description;
    
    @Column
    private Integer maxUsers;
    
    @Column
    private LocalDateTime createdTime;
    
    @Column
    private LocalDateTime modifiedTime;
}
