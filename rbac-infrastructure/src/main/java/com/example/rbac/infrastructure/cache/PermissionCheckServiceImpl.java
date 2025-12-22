package com.example.rbac.infrastructure.cache;

import com.example.rbac.core.domain.model.permission.Permission;
import com.example.rbac.core.domain.model.role.Role;
import com.example.rbac.core.domain.model.user.User;
import com.example.rbac.core.domain.service.PermissionCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限校验服务实现 - 带缓存
 */
@Service
@RequiredArgsConstructor
public class PermissionCheckServiceImpl implements PermissionCheckService {
    
    private static final String PERMISSION_CACHE = "user_permissions";
    private static final String ROLE_CACHE = "user_roles";
    
    @Override
    @Cacheable(value = PERMISSION_CACHE, key = "#user.id")
    public boolean checkPermission(User user, String resource, String action) {
        return user.getRoles().stream()
            .filter(role -> role.getEnabled() != null && role.getEnabled())
            .flatMap(role -> role.getPermissions().stream())
            .anyMatch(permission -> 
                resource.equals(permission.getResource()) &&
                action.equals(permission.getAction())
            );
    }
    
    @Override
    @Cacheable(value = ROLE_CACHE, key = "#user.id")
    public boolean checkRole(User user, String roleCode) {
        return user.getRoles().stream()
            .anyMatch(role -> roleCode.equals(role.getCode()) && 
                (role.getEnabled() == null || role.getEnabled())
            );
    }
    
    @Override
    public boolean hasPermission(Role role, String permissionCode) {
        return role.getPermissions().stream()
            .anyMatch(permission -> permissionCode.equals(permission.getCode()));
    }
    
    @Override
    @Cacheable(value = PERMISSION_CACHE, key = "#user.id + '_codes'")
    public Set<String> getUserPermissions(User user) {
        return user.getRoles().stream()
            .filter(role -> role.getEnabled() != null && role.getEnabled())
            .flatMap(role -> role.getPermissions().stream())
            .map(Permission::getCode)
            .collect(Collectors.toSet());
    }
    
    /**
     * 清除用户权限缓存
     */
    @CacheEvict(value = {PERMISSION_CACHE, ROLE_CACHE}, key = "#userId")
    public void clearUserPermissionCache(Long userId) {
        // 缓存清除
    }
}

