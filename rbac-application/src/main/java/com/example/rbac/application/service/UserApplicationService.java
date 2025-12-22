package com.example.rbac.application.service;

import com.example.rbac.core.domain.model.user.User;
import com.example.rbac.core.domain.model.role.Role;
import com.example.rbac.core.domain.repository.UserRepository;
import com.example.rbac.core.domain.repository.RoleRepository;
import com.example.rbac.core.domain.service.DomainEventPublisher;
import com.example.rbac.application.command.CreateUserCommand;
import com.example.rbac.application.command.AssignRolesCommand;
import com.example.rbac.application.dto.UserDTO;
import com.example.rbac.application.dto.UserPermissionsDTO;
import com.example.rbac.application.assembler.UserAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 用户应用服务
 */
@Service
@RequiredArgsConstructor
public class UserApplicationService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DomainEventPublisher domainEventPublisher;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * 创建用户
     */
    @Transactional
    public UserDTO createUser(CreateUserCommand command) {
        // 检查用户名唯一性
        Optional<User> existUser = userRepository.findByUsername(command.getUsername());
        if (existUser.isPresent()) {
            throw new BusinessException("用户名已存在: " + command.getUsername());
        }
        
        // 创建用户
        User user = new User(
            command.getUsername(),
            command.getEmail(),
            passwordEncoder.encode(command.getPassword()),
            command.getTenantId()
        );
        user.setPhone(command.getPhone());
        
        // 分配初始角色
        if (command.getRoleIds() != null && !command.getRoleIds().isEmpty()) {
            for (Long roleId : command.getRoleIds()) {
                Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new BusinessException("角色不存在: " + roleId));
                user.assignRole(role);
            }
        }
        
        // 保存用户
        User savedUser = userRepository.save(user);
        
        // 发布领域事件
        savedUser.getDomainEvents().forEach(domainEventPublisher::publish);
        savedUser.clearDomainEvents();
        
        return UserAssembler.toDTO(savedUser);
    }
    
    /**
     * 获取用户详情
     */
    @Transactional(readOnly = true)
    public UserDTO getUserDetail(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException("用户不存在: " + userId));
        return UserAssembler.toDTO(user);
    }
    
    /**
     * 获取用户权限
     */
    @Transactional(readOnly = true)
    public UserPermissionsDTO getUserPermissions(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException("用户不存在: " + userId));
        return UserAssembler.toPermissionDTO(user);
    }
    
    /**
     * 分配角色
     */
    @Transactional
    public UserDTO assignRoles(AssignRolesCommand command) {
        User user = userRepository.findById(command.getUserId())
            .orElseThrow(() -> new BusinessException("用户不存在: " + command.getUserId()));
        
        // 清空现有角色
        user.getRoles().clear();
        
        // 分配新角色
        if (command.getRoleIds() != null && !command.getRoleIds().isEmpty()) {
            for (Long roleId : command.getRoleIds()) {
                Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new BusinessException("角色不存在: " + roleId));
                user.assignRole(role);
            }
        }
        
        // 保存用户
        User savedUser = userRepository.save(user);
        
        // 发布领域事件
        savedUser.getDomainEvents().forEach(domainEventPublisher::publish);
        savedUser.clearDomainEvents();
        
        return UserAssembler.toDTO(savedUser);
    }
    
    /**
     * 锁定用户
     */
    @Transactional
    public UserDTO lockUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException("用户不存在: " + userId));
        user.lock();
        User savedUser = userRepository.save(user);
        savedUser.getDomainEvents().forEach(domainEventPublisher::publish);
        savedUser.clearDomainEvents();
        return UserAssembler.toDTO(savedUser);
    }
    
    /**
     * 激活用户
     */
    @Transactional
    public UserDTO activateUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException("用户不存在: " + userId));
        user.activate();
        User savedUser = userRepository.save(user);
        savedUser.getDomainEvents().forEach(domainEventPublisher::publish);
        savedUser.clearDomainEvents();
        return UserAssembler.toDTO(savedUser);
    }
}
