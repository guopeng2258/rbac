package com.example.rbac.web.config;

import com.example.rbac.application.service.BusinessException;
import com.example.rbac.web.aop.PermissionAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 业务异常处理
     */
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return ApiResponse.error(-1, e.getMessage());
    }
    
    /**
     * 权限异常处理
     */
    @ExceptionHandler(PermissionAspect.AccessDeniedException.class)
    public ApiResponse<Void> handleAccessDeniedException(PermissionAspect.AccessDeniedException e) {
        log.warn("权限异常: {}", e.getMessage());
        return ApiResponse.error(403, e.getMessage());
    }
    
    /**
     * 通用异常处理
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return ApiResponse.error(-500, "系统异常，请联系管理员");
    }
}
