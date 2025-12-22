package com.example.rbac.core.domain.model.user;

import com.example.rbac.core.domain.model.DomainEvent;

/**
 * 用户激活事件
 */
public class UserActivatedEvent extends DomainEvent {
    
    public UserActivatedEvent(String userId) {
        super(userId);
    }
}

