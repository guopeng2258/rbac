package com.example.rbac.application.service;

import com.example.rbac.core.domain.model.role.Role;
import com.example.rbac.core.domain.model.role.RoleType;
import com.example.rbac.core.domain.model.permission.Permission;
import com.example.rbac.core.domain.repository.RoleRepository;
import com.example.rbac.core.domain.repository.PermissionRepository;
import com.example.rbac.core.domain.service.DomainEventPublisher;
import com.example.rbac.application.command.CreateRoleCommand;
import com.example.rbac.application.dto.RoleDTO;
import com.example.rbac.application.assembler.RoleAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色应用服务
 */
@Service
@RequiredArgsConstructor
public class RoleApplicationService {
    
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final DomainEventPublisher domainEventPublisher;
    
    /**
     * 创建角色
     */
    @Transactional
    public RoleDTO createRole(CreateRoleCommand command) {
        // 检查角色编码唯一性
        if (roleRepository.findByCodeAndTenantId(command.getCode(), command.getTenantId()).isPresent()) {
            throw new BusinessException("角色编码已存在: " + command.getCode());
        }
        
        // 创建角色
        Role role = new Role(
            command.getCode(),
            command.getName(),
            RoleType.valueOf(command.getType()),
            command.getTenantId()
        );
        role.setDescription(command.getDescription());
        
        // 保存角色
        Role savedRole = roleRepository.save(role);
        
        return RoleAssembler.toDTO(savedRole);
    }
    
    /**
     * 获取角色详情
     */
    @Transactional(readOnly = true)
    public RoleDTO getRoleDetail(Long roleId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new BusinessException("角色不存在: " + roleId));
        return RoleAssembler.toDTO(role);
    }
    
    /**
     * 给角色添加权限
     */
    @Transactional
    public RoleDTO addPermissionToRole(Long roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new BusinessException("角色不存在: " + roleId));
        
        Permission permission = permissionRepository.findById(permissionId)
            .orElseThrow(() -> new BusinessException("权限不存在: " + permissionId));
        
        role.addPermission(permission);
        Role savedRole = roleRepository.save(role);
        
        savedRole.getDomainEvents().forEach(domainEventPublisher::publish);
        savedRole.clearDomainEvents();
        
        return RoleAssembler.toDTO(savedRole);
    }
    
    /**
     * 从角色移除权限
     */
    @Transactional
    public RoleDTO removePermissionFromRole(Long roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new BusinessException("角色不存在: " + roleId));
        
        Permission permission = permissionRepository.findById(permissionId)
            .orElseThrow(() -> new BusinessException("权限不存在: " + permissionId));
        
        role.removePermission(permission);
        Role savedRole = roleRepository.save(role);
        
        savedRole.getDomainEvents().forEach(domainEventPublisher::publish);
        savedRole.clearDomainEvents();
        
        return RoleAssembler.toDTO(savedRole);
    }
    
    /**
     * 启用角色
     */
    @Transactional
    public RoleDTO enableRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new BusinessException("角色不存在: " + roleId));
        role.enable();
        Role savedRole = roleRepository.save(role);
        savedRole.getDomainEvents().forEach(domainEventPublisher::publish);
        savedRole.clearDomainEvents();
        return RoleAssembler.toDTO(savedRole);
    }
    
    /**
     * 禁用角色
     */
    @Transactional
    public RoleDTO disableRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new BusinessException("角色不存在: " + roleId));
        role.disable();
        Role savedRole = roleRepository.save(role);
        savedRole.getDomainEvents().forEach(domainEventPublisher::publish);
        savedRole.clearDomainEvents();
        return RoleAssembler.toDTO(savedRole);
    }
    
    /**
     * 获取租户的所有角色
     */
    @Transactional(readOnly = true)
    public List<RoleDTO> listRolesByTenant(Long tenantId) {
        List<Role> roles = roleRepository.findByTenantId(tenantId);
        return roles.stream()
                .map(RoleAssembler::toDTO)
                .collect(Collectors.toList());
    }
}
