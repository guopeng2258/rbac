# Query 和 Event 使用示例

本文档展示如何使用项目中的Query（查询）和Event（事件）功能。

---

## 📋 一、Query（查询）使用示例

### 1.1 基本概念

Query对象用于封装复杂的查询条件，遵循CQRS（命令查询职责分离）模式。

**优势：**
- ✅ 查询逻辑与命令逻辑分离
- ✅ 支持复杂的组合查询
- ✅ 便于缓存优化
- ✅ 提高代码可读性

### 1.2 Query对象示例

#### UserQuery - 用户查询对象

```java
UserQuery query = UserQuery.builder()
    .username("admin")              // 用户名模糊查询
    .email("@example.com")          // 邮箱模糊查询
    .status("ACTIVE")               // 状态精确匹配
    .tenantId(1L)                   // 租户ID
    .roleCode("admin")              // 角色编码
    .includeRoles(true)             // 包含角色信息
    .includePermissions(true)       // 包含权限信息
    .page(0)                        // 第0页
    .size(10)                       // 每页10条
    .sortBy("createdTime")          // 按创建时间排序
    .sortDirection("DESC")          // 降序
    .build();
```

#### RoleQuery - 角色查询对象

```java
RoleQuery query = RoleQuery.builder()
    .code("admin")
    .name("管理")                   // 名称模糊查询
    .type("SYSTEM")
    .enabled(true)
    .tenantId(1L)
    .includePermissions(true)
    .page(0)
    .size(20)
    .build();
```

#### PermissionQuery - 权限查询对象

```java
PermissionQuery query = PermissionQuery.builder()
    .code("user:")                  // 权限编码前缀查询
    .type("API")                    // 权限类型
    .resource("user")               // 资源名称
    .action("view")                 // 操作名称
    .tenantId(1L)
    .page(0)
    .size(50)
    .build();
```

---

## 🎯 二、Query API 使用示例

### 2.1 复杂条件查询用户

**请求：**
```bash
curl -X POST http://localhost:8080/api/query/users/search \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "username": "admin",
    "status": "ACTIVE",
    "tenantId": 1,
    "roleCode": "admin",
    "includeRoles": true,
    "page": 0,
    "size": 10
  }'
```

**响应：**
```json
{
  "code": 0,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "username": "admin",
      "email": "admin@example.com",
      "status": "ACTIVE",
      "roles": ["admin", "user"],
      "tenantId": 1
    }
  ]
}
```

### 2.2 根据用户名查询

**请求：**
```bash
curl http://localhost:8080/api/query/users/by-username/admin \
  -H "Authorization: Bearer <token>"
```

### 2.3 查询用户完整权限

**请求：**
```bash
curl http://localhost:8080/api/query/users/1/full-permissions \
  -H "Authorization: Bearer <token>"
```

**响应：**
```json
{
  "code": 0,
  "message": "成功",
  "data": {
    "userId": 1,
    "username": "admin",
    "tenantId": 1,
    "roles": ["admin", "user"],
    "permissions": [
      "user:view",
      "user:create",
      "user:edit",
      "role:manage"
    ]
  }
}
```

### 2.4 统计租户用户数

**请求：**
```bash
curl http://localhost:8080/api/query/users/count/tenant/1 \
  -H "Authorization: Bearer <token>"
```

**响应：**
```json
{
  "code": 0,
  "message": "成功",
  "data": 15
}
```

### 2.5 检查用户名是否存在

**请求：**
```bash
curl http://localhost:8080/api/query/users/exists/admin \
  -H "Authorization: Bearer <token>"
```

**响应：**
```json
{
  "code": 0,
  "message": "成功",
  "data": true
}
```

---

## 🎪 三、Event（事件）使用示例

### 3.1 事件驱动架构

当领域对象状态变化时，会自动发布领域事件，事件监听器会响应这些事件。

**事件流程：**
```
1. 领域对象状态变化
   ↓
2. 注册领域事件
   ↓
3. 应用服务保存对象
   ↓
4. 发布领域事件
   ↓
5. 事件监听器响应
   ↓
6. 执行副作用（审计日志、缓存清理、通知等）
```

### 3.2 已实现的事件监听器

#### 1️⃣ AuditLogEventListener - 审计日志监听器

**监听的事件：**
- ✅ UserRoleAssignedEvent - 用户角色分配
- ✅ UserLockedEvent - 用户锁定
- ✅ UserActivatedEvent - 用户激活
- ✅ RolePermissionAddedEvent - 角色权限添加
- ✅ RoleEnabledEvent - 角色启用
- ✅ RoleDisabledEvent - 角色禁用

**功能：**
- 记录所有重要操作的审计日志
- 支持追溯和合规审查

**日志示例：**
```
2025-12-22 15:30:45 INFO  【审计日志】用户角色分配 - 时间: 2025-12-22T15:30:45, 用户ID: 1, 角色ID: 2
2025-12-22 15:31:20 WARN  【审计日志】用户锁定 - 时间: 2025-12-22T15:31:20, 用户ID: 5
```

#### 2️⃣ PermissionCacheEventListener - 权限缓存监听器

**监听的事件：**
- ✅ UserRoleAssignedEvent - 用户角色变更
- ✅ RolePermissionChanged - 角色权限变更

**功能：**
- 自动清除过期的权限缓存
- 保证权限数据的实时性

**日志示例：**
```
2025-12-22 15:30:45 INFO  【缓存清理】用户角色变更，清除用户权限缓存 - 用户ID: 1
```

#### 3️⃣ NotificationEventListener - 通知监听器

**监听的事件：**
- ✅ UserLockedEvent - 用户锁定

**功能：**
- 发送邮件通知
- 发送站内信
- 发送短信（可扩展）

**通知示例：**
```
主题：账户已被锁定
内容：您的账户已被管理员锁定，如有疑问请联系管理员。
```

### 3.3 事件触发示例

#### 示例1：分配角色触发事件

```java
// 1. 用户分配角色
User user = userRepository.findById(1L).get();
Role role = roleRepository.findById(2L).get();
user.assignRole(role);  // 这里会注册 UserRoleAssignedEvent

// 2. 保存用户
userRepository.save(user);

// 3. 发布事件
user.getDomainEvents().forEach(domainEventPublisher::publish);

// 4. 事件监听器自动响应
// - AuditLogEventListener 记录审计日志
// - PermissionCacheEventListener 清除权限缓存
```

**控制台输出：**
```
INFO  发布领域事件: UserRoleAssignedEvent
INFO  【审计日志】用户角色分配 - 用户ID: 1, 角色ID: 2
INFO  【缓存清理】用户角色变更，清除用户权限缓存 - 用户ID: 1
```

#### 示例2：锁定用户触发事件

```java
// 1. 锁定用户
User user = userRepository.findById(5L).get();
user.lock();  // 注册 UserLockedEvent

// 2. 保存并发布事件
userRepository.save(user);
user.getDomainEvents().forEach(domainEventPublisher::publish);

// 3. 多个监听器响应
// - AuditLogEventListener 记录审计日志
// - NotificationEventListener 发送通知
```

**控制台输出：**
```
INFO  发布领域事件: UserLockedEvent
WARN  【审计日志】用户锁定 - 用户ID: 5
INFO  【通知】用户被锁定，发送通知 - 用户ID: 5
```

---

## 🔧 四、扩展开发指南

### 4.1 添加新的Query对象

```java
@Data
@Builder
public class CustomQuery {
    private String field1;
    private String field2;
    // ... 其他查询字段
}
```

### 4.2 添加新的QueryService

```java
@Service
@RequiredArgsConstructor
public class CustomQueryService {
    
    private final CustomRepository repository;
    
    @Transactional(readOnly = true)
    public List<CustomDTO> query(CustomQuery query) {
        // 实现查询逻辑
        return repository.findByConditions(query);
    }
}
```

### 4.3 添加新的事件监听器

```java
@Slf4j
@Component
public class CustomEventListener {
    
    public void onCustomEvent(CustomEvent event) {
        log.info("处理自定义事件: {}", event);
        // 实现事件处理逻辑
    }
}
```

### 4.4 在DomainEventPublisher中注册新事件

```java
@Override
public void publish(DomainEvent event) {
    // ... 现有代码
    
    // 添加新事件分发
    else if (event instanceof CustomEvent) {
        CustomEvent e = (CustomEvent) event;
        customEventListener.onCustomEvent(e);
    }
}
```

---

## 📊 五、架构优势

### CQRS模式优势

| 特性 | 说明 |
|------|------|
| **职责分离** | 查询和命令逻辑分离，代码更清晰 |
| **性能优化** | 查询可以使用专门的只读副本 |
| **缓存友好** | 查询结果易于缓存 |
| **扩展性强** | 查询和命令可以独立扩展 |

### 事件驱动优势

| 特性 | 说明 |
|------|------|
| **解耦** | 业务逻辑与副作用分离 |
| **可追溯** | 所有事件都有记录 |
| **易扩展** | 添加新监听器不影响现有代码 |
| **异步处理** | 可以将事件处理改为异步 |

---

## 🎯 六、最佳实践

### Query最佳实践

1. ✅ 使用Builder模式构建Query对象
2. ✅ Query对象应该是不可变的
3. ✅ 查询方法使用 `@Transactional(readOnly = true)`
4. ✅ 复杂查询考虑使用Specification模式
5. ✅ 查询结果考虑缓存

### Event最佳实践

1. ✅ 事件应该是不可变的
2. ✅ 事件名称使用过去式（表示已发生）
3. ✅ 事件处理失败不应影响主流程
4. ✅ 重要事件应该持久化
5. ✅ 考虑使用消息队列处理异步事件

---

## 📚 参考资料

- [CQRS Pattern - Martin Fowler](https://martinfowler.com/bliki/CQRS.html)
- [Event Sourcing - Greg Young](https://www.eventstore.com/event-sourcing)
- [Domain Events - DDD](https://www.domainlanguage.com/ddd/)

---

## 总结

✅ **Query功能已实现：**
- UserQuery, RoleQuery, PermissionQuery
- UserQueryService
- UserQueryController

✅ **Event功能已实现：**
- AuditLogEventListener（审计日志）
- PermissionCacheEventListener（缓存清理）
- NotificationEventListener（通知发送）
- DomainEventPublisherImpl（事件分发）

现在您的RBAC系统支持完整的CQRS和事件驱动架构！🎉

