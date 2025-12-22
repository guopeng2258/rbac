package com.example.rbac.infrastructure.event;

import com.example.rbac.core.domain.model.user.UserLockedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 通知事件监听器
 * 发送各种通知（邮件、短信、站内信等）
 */
@Slf4j
@Component
public class NotificationEventListener {
    
    /**
     * 监听用户锁定事件，发送通知
     */
    public void onUserLocked(UserLockedEvent event) {
        log.info("【通知】用户被锁定，发送通知 - 用户ID: {}", event.getAggregateId());
        
        // TODO: 实现发送邮件通知
        sendEmail(event.getAggregateId(), "账户已被锁定", 
                "您的账户已被管理员锁定，如有疑问请联系管理员。");
        
        // TODO: 实现发送站内信
        sendInternalMessage(event.getAggregateId(), "账户锁定通知");
    }
    
    /**
     * 发送邮件
     */
    private void sendEmail(String userId, String subject, String content) {
        log.debug("发送邮件 - 用户ID: {}, 主题: {}", userId, subject);
        // TODO: 集成邮件服务
    }
    
    /**
     * 发送站内信
     */
    private void sendInternalMessage(String userId, String message) {
        log.debug("发送站内信 - 用户ID: {}, 消息: {}", userId, message);
        // TODO: 实现站内信发送
    }
}

