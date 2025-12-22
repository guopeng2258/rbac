# 基于DDD的Java RBAC系统

这是一个完整的基于领域驱动设计（DDD）的Java RBAC（基于角色的访问控制）系统实现。

## 项目结构

```
rbac-ddd-system/
├── rbac-core/              # 核心域模块
│   └── domain/            # 领域模型、仓储接口、领域服务
├── rbac-infrastructure/   # 基础设施层
│   └── persistence/       # JPA实现、Mapper转换
├── rbac-application/      # 应用服务层
│   ├── service/          # 应用服务
│   ├── command/          # 命令对象
│   ├── dto/              # 数据传输对象
│   └── assembler/        # 数据组装器
└── rbac-interface/        # Web接口层
    └── web/              # Web层
        ├── controller/   # REST控制器
        ├── security/     # 安全认证
        ├── aop/          # AOP切面
        └── config/       # 配置类
```

## 核心功能

### 1. 用户管理
- 用户创建、查询、更新
- 用户角色管理
- 用户状态管理（活跃、锁定等）

### 2. 角色管理
- 角色创建、查询、启用/禁用
- 角色权限关联
- 基于角色的权限控制

### 3. 权限管理
- 权限定义（菜单、按钮、API、数据）
- 权限分配给角色
- 权限校验

### 4. 多租户支持
- 租户隔离
- 租户级别的权限管理

### 5. 安全认证
- JWT令牌认证
- Spring Security集成
- 自定义权限检查AOP

## 技术栈

- **框架**: Spring Boot 2.7.15
- **数据库**: H2(开发环境)
- **ORM**: JPA/Hibernate
- **认证**: JWT + Spring Security
- **缓存**: Spring Cache
- **其他**: Lombok, MapStruct

## API端点

### 用户管理
- `POST /api/users` - 创建用户
- `GET /api/users/{userId}` - 获取用户详情
- `GET /api/users/{userId}/permissions` - 获取用户权限
- `PUT /api/users/{userId}/roles` - 分配角色
- `POST /api/users/{userId}/lock` - 锁定用户
- `POST /api/users/{userId}/activate` - 激活用户

### 角色管理
- `POST /api/roles` - 创建角色
- `GET /api/roles/{roleId}` - 获取角色详情
- `POST /api/roles/{roleId}/permissions/{permissionId}` - 添加权限
- `DELETE /api/roles/{roleId}/permissions/{permissionId}` - 移除权限
- `POST /api/roles/{roleId}/enable` - 启用角色
- `POST /api/roles/{roleId}/disable` - 禁用角色
- `GET /api/roles/tenant/{tenantId}` - 获取租户角色列表

### 权限管理
- `POST /api/permissions` - 创建权限
- `GET /api/permissions/{permissionId}` - 获取权限详情
- `GET /api/permissions/tenant/{tenantId}` - 查询租户权限
- `DELETE /api/permissions/{permissionId}` - 删除权限

## 启动应用

```bash
mvn clean install
mvn spring-boot:run -pl rbac-interface
```

应用将在 `http://localhost:8080` 启动

## 数据库配置

默认使用H2内存数据库，配置在 `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:rbacdb
    driver-class-name: org.h2.Driver
  h2:
    console:
      enabled: true
```

可访问 H2 控制台: `http://localhost:8080/h2-console`

## DDD设计特点

1. **清晰的分层架构**
   - 领域层：业务逻辑和聚合
   - 应用层：应用协调
   - 基础设施层：技术实现
   - 接口层：对外API

2. **丰富的领域模型**
   - 聚合根：User, Role, Permission, Tenant
   - 值对象：支持领域语义
   - 领域事件：支持事件驱动

3. **低耦合高内聚**
   - 仓储模式隔离数据访问
   - 应用服务协调领域服务
   - 依赖倒置原则应用

4. **可扩展的权限检查**
   - 支持多层次权限
   - AOP声明式权限控制
   - 细粒度权限校验

## 发展方向

- [ ] 添加权限缓存机制
- [ ] 支持权限继承
- [ ] 添加审计日志
- [ ] 支持角色分级
- [ ] API限流和速率控制
- [ ] 单元测试覆盖
