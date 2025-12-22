package com.example.rbac.application.service;

import com.example.rbac.core.domain.model.permission.Permission;
import com.example.rbac.core.domain.model.permission.PermissionType;
import com.example.rbac.core.domain.repository.PermissionRepository;
import com.example.rbac.core.domain.service.DomainEventPublisher;
import com.example.rbac.application.command.CreatePermissionCommand;
import com.example.rbac.application.dto.PermissionDTO;
import com.example.rbac.application.assembler.PermissionAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限应用服务
 */
@Service
@RequiredArgsConstructor
public class PermissionApplicationService {
    
    private final PermissionRepository permissionRepository;
    private final DomainEventPublisher domainEventPublisher;
    
    /**
     * 创建权限
     */
    @Transactional
    public PermissionDTO createPermission(CreatePermissionCommand command) {
        // 检查权限编码唯一性
        if (permissionRepository.findByCodeAndTenantId(command.getCode(), command.getTenantId()).isPresent()) {
            throw new BusinessException("权限编码已存在: " + command.getCode());
        }
        
        // 创建权限
        Permission permission = new Permission(
            command.getCode(),
            command.getName(),
            PermissionType.valueOf(command.getType()),
            command.getTenantId()
        );
        permission.setDescription(command.getDescription());
        permission.setResource(command.getResource());
        permission.setAction(command.getAction());
        permission.setParentId(command.getParentId());
        permission.setSortOrder(command.getSortOrder());
        
        // 保存权限
        Permission savedPermission = permissionRepository.save(permission);
        
        return PermissionAssembler.toDTO(savedPermission);
    }
    
    /**
     * 获取权限详情
     */
    @Transactional(readOnly = true)
    public PermissionDTO getPermissionDetail(Long permissionId) {
        Permission permission = permissionRepository.findById(permissionId)
            .orElseThrow(() -> new BusinessException("权限不存在: " + permissionId));
        return PermissionAssembler.toDTO(permission);
    }
    
    /**
     * 查询租户的所有权限
     */
    @Transactional(readOnly = true)
    public List<PermissionDTO> listPermissions(Long tenantId) {
        List<Permission> permissions = permissionRepository.findByTenantId(tenantId);
        return permissions.stream()
                .map(PermissionAssembler::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 删除权限
     */
    @Transactional
    public void deletePermission(Long permissionId) {
        Permission permission = permissionRepository.findById(permissionId)
            .orElseThrow(() -> new BusinessException("权限不存在: " + permissionId));
        permissionRepository.delete(permissionId);
    }
}
