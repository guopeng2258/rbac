# 快速启动指南

## 构建项目

```bash
cd /Users/perry.guo/project/rbac
mvn clean package -DskipTests
```

## 运行应用

```bash
# 方式1: 直接运行JAR
java -jar rbac-interface/target/rbac-interface-1.0.0.jar

# 方式2: 使用Maven运行
mvn spring-boot:run -pl rbac-interface
```

应用将在 `http://localhost:8080` 启动

## 核心特性

### ✅ 已完成的功能
- [x] 用户管理（创建、查询、更新状态）
- [x] 角色管理（创建、启用/禁用、权限分配）
- [x] 权限管理（四种权限类型：菜单、按钮、API、数据）
- [x] 多租户支持（租户隔离）
- [x] JWT认证（Bearer Token）
- [x] Spring Security集成
- [x] 声明式权限检查（@RequirePermission AOP）
- [x] 领域事件系统
- [x] 缓存支持

### 📋 DDD架构分层
```
rbac-ddd-system/
├── rbac-core/              # 领域层（User、Role、Permission聚合根）
├── rbac-application/       # 应用服务层（协调和编排）
├── rbac-infrastructure/    # 基础设施层（JPA、缓存、事件发布）
└── rbac-interface/         # 接口层（REST API、安全配置）
```

## API调用示例

### 1. 创建权限

```bash
curl -X POST http://localhost:8080/api/permissions \
  -H "Content-Type: application/json" \
  -d '{
    "code": "user:view",
    "name": "查看用户",
    "type": "API",
    "resource": "user",
    "action": "view",
    "tenantId": 1,
    "sortOrder": 1
  }'
```

### 2. 创建角色

```bash
curl -X POST http://localhost:8080/api/roles \
  -H "Content-Type: application/json" \
  -d '{
    "code": "admin",
    "name": "管理员",
    "type": "SYSTEM",
    "description": "系统管理员",
    "tenantId": 1
  }'
```

### 3. 添加权限到角色

```bash
curl -X POST http://localhost:8080/api/roles/1/permissions/1 \
  -H "Authorization: Bearer <token>"
```

### 4. 创建用户

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "email": "admin@example.com",
    "password": "admin123",
    "phone": "13800138000",
    "tenantId": 1,
    "roleIds": [1]
  }'
```

### 5. 获取用户权限

```bash
curl http://localhost:8080/api/users/1/permissions \
  -H "Authorization: Bearer <token>"
```

## 初始化数据

应用启动时会自动初始化示例数据（如果不是测试环境）：
- 3个权限（user:view, user:create, role:manage）
- 2个角色（admin, user）
- 2个用户（admin, user）

### 演示账号

| 用户名 | 密码 | 角色 | 权限 |
|--------|------|------|------|
| admin | admin123 | 管理员 | user:view, user:create, role:manage |
| user | user123 | 普通用户 | user:view |

## 数据库

- 开发环境：H2内存数据库
- H2控制台：`http://localhost:8080/h2-console`
- JDBC URL：`jdbc:h2:mem:rbacdb`
- 用户名：`sa`
- 密码：留空

## 性能优化建议

1. **缓存策略**
   - 权限缓存：在创建/修改权限后自动刷新
   - 用户权限缓存：支持TTL设置

2. **数据库优化**
   - 添加索引到 `code` 和 `tenantId` 字段
   - 使用分区表管理日志数据

3. **并发控制**
   - 使用乐观锁处理并发更新
   - 实现分布式锁机制

## 常见问题

### Q: 如何修改JWT过期时间？
A: 在 `application.yml` 中修改 `jwt.expiration` 属性（毫秒）

### Q: 如何集成真实数据库？
A: 修改 `application.yml` 中的数据源配置，例如：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/rbac
    username: root
    password: password
```

### Q: 如何自定义权限检查逻辑？
A: 修改 `PermissionAspect.java` 中的权限验证逻辑

## 扩展开发

### 添加新的聚合根
1. 在 `rbac-core/domain/model` 下创建新的实体
2. 在 `rbac-core/domain/repository` 下定义仓储接口
3. 在 `rbac-infrastructure/persistence` 下实现仓储
4. 在 `rbac-application/service` 下创建应用服务
5. 在 `rbac-interface/controller` 下暴露REST API

### 集成消息队列
将 `DomainEventPublisherImpl` 改为使用 RabbitMQ/Kafka 发送事件

### 添加审计日志
创建 `AuditLog` 聚合根，监听领域事件并记录

## 许可证

MIT

