package com.example.rbac.web.aop;

import java.lang.annotation.*;

/**
 * 权限检查注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {
    
    /**
     * 权限编码，支持多个权限使用逗号分隔
     */
    String value();
    
    /**
     * 逻辑运算：AND(全部满足), OR(任意满足)
     */
    Logical logical() default Logical.AND;
    
    enum Logical {
        AND, OR
    }
}
