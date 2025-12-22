package com.example.rbac.web.aop;

import com.example.rbac.web.security.UserContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 权限检查切面
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class PermissionAspect {
    
    private final UserContextHolder userContextHolder;
    
    @Around("@annotation(requirePermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequirePermission requirePermission) 
        throws Throwable {
        
        UserContextHolder.UserContext userContext = userContextHolder.getCurrentUser();
        if (userContext == null) {
            throw new AccessDeniedException("用户未登录");
        }
        
        String[] permissions = requirePermission.value().split(",");
        boolean hasPermission;
        
        if (requirePermission.logical() == RequirePermission.Logical.AND) {
            hasPermission = Arrays.stream(permissions)
                    .allMatch(permission -> userContext.hasPermission(permission.trim()));
        } else {
            hasPermission = Arrays.stream(permissions)
                    .anyMatch(permission -> userContext.hasPermission(permission.trim()));
        }
        
        if (!hasPermission) {
            log.warn("用户 {} 权限不足，尝试访问: {}", userContext.getUsername(), requirePermission.value());
            throw new AccessDeniedException("权限不足");
        }
        
        return joinPoint.proceed();
    }
    
    public static class AccessDeniedException extends RuntimeException {
        public AccessDeniedException(String message) {
            super(message);
        }
    }
}
