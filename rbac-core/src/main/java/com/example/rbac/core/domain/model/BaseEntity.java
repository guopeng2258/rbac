package com.example.rbac.core.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 聚合根基类
 */
@Getter
@Setter
public abstract class BaseEntity<ID> {
    
    protected ID id;
    
    protected LocalDateTime createdTime;
    
    protected LocalDateTime modifiedTime;
    
    private List<DomainEvent> domainEvents = new ArrayList<>();
    
    /**
     * 注册领域事件
     */
    protected void registerEvent(DomainEvent event) {
        if (domainEvents == null) {
            domainEvents = new ArrayList<>();
        }
        domainEvents.add(event);
    }
    
    /**
     * 获取所有未发布的领域事件
     */
    public List<DomainEvent> getDomainEvents() {
        return domainEvents;
    }
    
    /**
     * 清空已发布的事件
     */
    public void clearDomainEvents() {
        domainEvents.clear();
    }
}

