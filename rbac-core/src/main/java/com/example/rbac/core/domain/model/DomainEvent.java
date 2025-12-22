package com.example.rbac.core.domain.model;

import java.time.LocalDateTime;

/**
 * 领域事件基类
 */
public abstract class DomainEvent {
    
    private final LocalDateTime occurredOn;
    private final String aggregateId;
    
    protected DomainEvent(String aggregateId) {
        this.aggregateId = aggregateId;
        this.occurredOn = LocalDateTime.now();
    }
    
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
    
    public String getAggregateId() {
        return aggregateId;
    }
}

