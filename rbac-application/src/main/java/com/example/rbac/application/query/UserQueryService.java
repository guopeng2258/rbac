package com.example.rbac.application.query;

import com.example.rbac.application.dto.UserDTO;
import com.example.rbac.application.dto.UserPermissionsDTO;
import com.example.rbac.core.domain.repository.UserRepository;
import com.example.rbac.application.assembler.UserAssembler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户查询服务
 * CQRS模式 - 查询端
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserQueryService {
    
    private final UserRepository userRepository;
    
    /**
     * 根据查询条件查询用户列表
     */
    @Transactional(readOnly = true)
    public List<UserDTO> queryUsers(UserQuery query) {
        log.info("查询用户列表，条件: {}", query);
        
        // 根据租户ID查询
        if (query.getTenantId() != null) {
            return userRepository.findByTenantId(query.getTenantId())
                    .stream()
                    .filter(user -> matchesQuery(user, query))
                    .map(UserAssembler::toDTO)
                    .collect(Collectors.toList());
        }
        
        // TODO: 实现更复杂的查询逻辑（可以使用Specification模式）
        return List.of();
    }
    
    /**
     * 根据用户名查询用户
     */
    @Transactional(readOnly = true)
    public UserDTO queryByUsername(String username) {
        log.info("根据用户名查询用户: {}", username);
        return userRepository.findByUsername(username)
                .map(UserAssembler::toDTO)
                .orElse(null);
    }
    
    /**
     * 查询用户的完整权限信息
     */
    @Transactional(readOnly = true)
    public UserPermissionsDTO queryUserPermissions(Long userId) {
        log.info("查询用户权限信息: userId={}", userId);
        return userRepository.findById(userId)
                .map(UserAssembler::toPermissionDTO)
                .orElse(null);
    }
    
    /**
     * 统计租户下的用户数量
     */
    @Transactional(readOnly = true)
    public long countUsersByTenant(Long tenantId) {
        log.info("统计租户用户数: tenantId={}", tenantId);
        return userRepository.findByTenantId(tenantId).size();
    }
    
    /**
     * 检查用户是否存在
     */
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
    
    /**
     * 匹配查询条件
     */
    private boolean matchesQuery(com.example.rbac.core.domain.model.user.User user, UserQuery query) {
        // 用户名模糊匹配
        if (query.getUsername() != null && 
            !user.getUsername().toLowerCase().contains(query.getUsername().toLowerCase())) {
            return false;
        }
        
        // 邮箱模糊匹配
        if (query.getEmail() != null && user.getEmail() != null &&
            !user.getEmail().toLowerCase().contains(query.getEmail().toLowerCase())) {
            return false;
        }
        
        // 状态精确匹配
        if (query.getStatus() != null && 
            !user.getStatus().name().equals(query.getStatus())) {
            return false;
        }
        
        // 角色匹配
        if (query.getRoleCode() != null) {
            boolean hasRole = user.getRoles().stream()
                    .anyMatch(role -> role.getCode().equals(query.getRoleCode()));
            if (!hasRole) {
                return false;
            }
        }
        
        return true;
    }
}

