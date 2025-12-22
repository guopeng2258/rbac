package com.example.rbac.test;

import com.example.rbac.application.command.CreatePermissionCommand;
import com.example.rbac.application.command.CreateRoleCommand;
import com.example.rbac.application.command.CreateUserCommand;
import com.example.rbac.application.service.PermissionApplicationService;
import com.example.rbac.application.service.RoleApplicationService;
import com.example.rbac.application.service.UserApplicationService;
import com.example.rbac.core.domain.model.permission.PermissionType;
import com.example.rbac.core.domain.model.role.RoleType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

/**
 * 初始化数据配置
 */
@Configuration
@Profile("!test")
public class DataInitializer {
    
    @Bean
    public CommandLineRunner initializeData(
        PermissionApplicationService permissionService,
        RoleApplicationService roleService,
        UserApplicationService userService) {
        
        return args -> {
            try {
                // 创建权限
                var userViewPerm = permissionService.createPermission(
                    CreatePermissionCommand.builder()
                        .code("user:view")
                        .name("查看用户")
                        .type(PermissionType.API.name())
                        .resource("user")
                        .action("view")
                        .tenantId(1L)
                        .sortOrder(1)
                        .build()
                );
                
                var userCreatePerm = permissionService.createPermission(
                    CreatePermissionCommand.builder()
                        .code("user:create")
                        .name("创建用户")
                        .type(PermissionType.API.name())
                        .resource("user")
                        .action("create")
                        .tenantId(1L)
                        .sortOrder(2)
                        .build()
                );
                
                var roleManagePerm = permissionService.createPermission(
                    CreatePermissionCommand.builder()
                        .code("role:manage")
                        .name("管理角色")
                        .type(PermissionType.API.name())
                        .resource("role")
                        .action("manage")
                        .tenantId(1L)
                        .sortOrder(3)
                        .build()
                );
                
                // 创建角色
                var adminRole = roleService.createRole(
                    CreateRoleCommand.builder()
                        .code("admin")
                        .name("管理员")
                        .type(RoleType.SYSTEM.name())
                        .tenantId(1L)
                        .permissionIds(Arrays.asList(
                            userViewPerm.getId(),
                            userCreatePerm.getId(),
                            roleManagePerm.getId()
                        ))
                        .build()
                );
                
                var userRole = roleService.createRole(
                    CreateRoleCommand.builder()
                        .code("user")
                        .name("普通用户")
                        .type(RoleType.SYSTEM.name())
                        .tenantId(1L)
                        .permissionIds(Arrays.asList(userViewPerm.getId()))
                        .build()
                );
                
                // 创建用户
                var adminUser = userService.createUser(
                    CreateUserCommand.builder()
                        .username("admin")
                        .email("admin@example.com")
                        .password("admin123")
                        .phone("13800138000")
                        .tenantId(1L)
                        .roleIds(Arrays.asList(adminRole.getId()))
                        .build()
                );
                
                var normalUser = userService.createUser(
                    CreateUserCommand.builder()
                        .username("user")
                        .email("user@example.com")
                        .password("user123")
                        .phone("13800138001")
                        .tenantId(1L)
                        .roleIds(Arrays.asList(userRole.getId()))
                        .build()
                );
                
                System.out.println("✓ 初始化数据完成");
                System.out.println("✓ 管理员用户: admin (密码: admin123)");
                System.out.println("✓ 普通用户: user (密码: user123)");
                
            } catch (Exception e) {
                System.err.println("✗ 初始化数据失败: " + e.getMessage());
            }
        };
    }
}

