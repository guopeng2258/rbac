package com.example.rbac.core.domain.service;

import com.example.rbac.core.domain.model.DomainEvent;

/**
 * 领域事件发布接口
 */
public interface DomainEventPublisher {
    
    /**
     * 发布领域事件
     */
    void publish(DomainEvent event);
}

