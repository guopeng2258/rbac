package com.example.rbac.web.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * 用户上下文持有器
 */
@Component
public class UserContextHolder {
    
    private static final ThreadLocal<UserContext> context = new ThreadLocal<>();
    
    public void setCurrentUser(UserContext userContext) {
        context.set(userContext);
    }
    
    public UserContext getCurrentUser() {
        return context.get();
    }
    
    public void clear() {
        context.remove();
    }
    
    public static class UserContext {
        private final Long userId;
        private final String username;
        private final Long tenantId;
        private final java.util.Set<String> roles;
        private final java.util.Set<String> permissions;
        
        public UserContext(Long userId, String username, Long tenantId, 
                         java.util.Set<String> roles, java.util.Set<String> permissions) {
            this.userId = userId;
            this.username = username;
            this.tenantId = tenantId;
            this.roles = roles;
            this.permissions = permissions;
        }
        
        public Long getUserId() {
            return userId;
        }
        
        public String getUsername() {
            return username;
        }
        
        public Long getTenantId() {
            return tenantId;
        }
        
        public java.util.Set<String> getRoles() {
            return roles;
        }
        
        public java.util.Set<String> getPermissions() {
            return permissions;
        }
        
        public boolean hasPermission(String permission) {
            return permissions != null && permissions.contains(permission);
        }
        
        public boolean hasRole(String role) {
            return roles != null && roles.contains(role);
        }
    }
}
