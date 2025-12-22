package com.example.rbac.core.domain.model.user;

import com.example.rbac.core.domain.model.DomainEvent;

/**
 * 用户锁定事件
 */
public class UserLockedEvent extends DomainEvent {
    
    public UserLockedEvent(String userId) {
        super(userId);
    }
}

