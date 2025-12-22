# MySQL 数据库初始化指南

## 快速启动

### 1. 使用 MySQL 命令行执行

```bash
# 连接到MySQL
mysql -u root -p

# 执行初始化脚本
mysql -u root -p < init-data.sql

# 或者在MySQL命令行中
source /path/to/init-data.sql;
```

### 2. 使用 MySQL Workbench

1. 打开 MySQL Workbench
2. 创建新连接或使用现有连接
3. 打开 `File` → `Open SQL Script` 选择 `init-data.sql`
4. 点击闪电图标执行或按 `Ctrl+Shift+Enter`

### 3. 使用 Navicat

1. 打开 Navicat Premium
2. 右键连接 → `Execute SQL File`
3. 选择 `init-data.sql` 文件
4. 点击执行

---

## 详细表结构说明

### 1. 租户表 (rbac_tenant)

```sql
DESC rbac_tenant;
```

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键，自增 |
| code | VARCHAR(50) | 租户编码，全局唯一 |
| name | VARCHAR(100) | 租户名称 |
| enabled | BOOLEAN | 是否启用 |
| description | VARCHAR(255) | 描述信息 |
| max_users | INT | 最大用户数 |
| created_time | TIMESTAMP | 创建时间 |
| modified_time | TIMESTAMP | 修改时间 |

**示例数据：**
```
id: 1, code: 'tenant_001', name: '默认租户'
id: 2, code: 'tenant_002', name: '测试租户'
```

---

### 2. 用户表 (rbac_user)

```sql
DESC rbac_user;
```

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键，自增 |
| username | VARCHAR(50) | 用户名，租户内唯一 |
| email | VARCHAR(100) | 邮箱地址 |
| password | VARCHAR(255) | 密码（BCrypt加密） |
| phone | VARCHAR(20) | 电话号码 |
| status | VARCHAR(20) | 状态：ACTIVE/INACTIVE/LOCKED/DELETED |
| last_login_time | TIMESTAMP | 最后登录时间 |
| tenant_id | BIGINT | 所属租户ID（外键） |
| created_time | TIMESTAMP | 创建时间 |
| modified_time | TIMESTAMP | 修改时间 |

**示例数据：**
```
id: 1, username: 'admin', email: 'admin@example.com', password: '(加密)', tenant_id: 1
id: 2, username: 'user01', email: 'user01@example.com', password: '(加密)', tenant_id: 1
```

**用户状态说明：**
- `ACTIVE` - 活跃，可正常登录
- `INACTIVE` - 不活跃
- `LOCKED` - 锁定，禁止登录
- `DELETED` - 删除

---

### 3. 权限表 (rbac_permission)

```sql
DESC rbac_permission;
```

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键，自增 |
| code | VARCHAR(100) | 权限编码，全局唯一 |
| name | VARCHAR(100) | 权限名称 |
| description | VARCHAR(255) | 权限描述 |
| type | VARCHAR(20) | 权限类型：MENU/BUTTON/API/DATA |
| resource | VARCHAR(100) | 资源名称 |
| action | VARCHAR(100) | 操作名称 |
| parent_id | BIGINT | 父权限ID（支持权限树）|
| sort_order | INT | 排序号 |
| tenant_id | BIGINT | 所属租户ID（外键） |
| created_time | TIMESTAMP | 创建时间 |
| modified_time | TIMESTAMP | 修改时间 |

**示例数据：**
```
id: 1, code: 'user:view', name: '查看用户', type: 'API', resource: 'user', action: 'view'
id: 2, code: 'user:create', name: '创建用户', type: 'API', resource: 'user', action: 'create'
```

**权限类型说明：**
- `MENU` - 菜单权限（前端菜单显示）
- `BUTTON` - 按钮权限（前端按钮显示）
- `API` - API权限（后端接口访问）
- `DATA` - 数据权限（数据级别控制）

---

### 4. 角色表 (rbac_role)

```sql
DESC rbac_role;
```

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键，自增 |
| code | VARCHAR(50) | 角色编码 |
| name | VARCHAR(100) | 角色名称 |
| description | VARCHAR(255) | 角色描述 |
| type | VARCHAR(20) | 角色类型：SYSTEM/CUSTOM |
| enabled | BOOLEAN | 是否启用 |
| tenant_id | BIGINT | 所属租户ID（外键） |
| created_time | TIMESTAMP | 创建时间 |
| modified_time | TIMESTAMP | 修改时间 |

**示例数据：**
```
id: 1, code: 'admin', name: '管理员', type: 'SYSTEM', enabled: 1, tenant_id: 1
id: 2, code: 'user', name: '普通用户', type: 'SYSTEM', enabled: 1, tenant_id: 1
```

**约束说明：**
- `(code, tenant_id)` 联合唯一索引
- 同一租户下角色编码唯一

---

### 5. 用户-角色关联表 (rbac_user_role)

```sql
DESC rbac_user_role;
```

| 字段 | 类型 | 说明 |
|------|------|------|
| user_id | BIGINT | 用户ID（外键）|
| role_id | BIGINT | 角色ID（外键）|

**关键说明：**
- 组合主键：`(user_id, role_id)`
- 支持一个用户多个角色
- 级联删除：删除用户或角色时自动删除关联记录

---

### 6. 角色-权限关联表 (rbac_role_permission)

```sql
DESC rbac_role_permission;
```

| 字段 | 类型 | 说明 |
|------|------|------|
| role_id | BIGINT | 角色ID（外键）|
| permission_id | BIGINT | 权限ID（外键）|

**关键说明：**
- 组合主键：`(role_id, permission_id)`
- 支持一个角色多个权限
- 级联删除：删除角色或权限时自动删除关联记录

---

## 常用查询

### 1. 查看用户的所有权限

```sql
SELECT DISTINCT p.id, p.code, p.name, p.type, p.resource, p.action
FROM rbac_user u
JOIN rbac_user_role ur ON u.id = ur.user_id
JOIN rbac_role r ON ur.role_id = r.id
JOIN rbac_role_permission rp ON r.id = rp.role_id
JOIN rbac_permission p ON rp.permission_id = p.id
WHERE u.username = 'admin' AND u.tenant_id = 1;
```

### 2. 查看角色的所有权限

```sql
SELECT p.id, p.code, p.name, p.type
FROM rbac_role r
JOIN rbac_role_permission rp ON r.id = rp.role_id
JOIN rbac_permission p ON rp.permission_id = p.id
WHERE r.code = 'admin' AND r.tenant_id = 1;
```

### 3. 查看用户的所有角色

```sql
SELECT r.id, r.code, r.name
FROM rbac_user u
JOIN rbac_user_role ur ON u.id = ur.user_id
JOIN rbac_role r ON ur.role_id = r.id
WHERE u.username = 'admin' AND u.tenant_id = 1;
```

### 4. 检查用户是否具有某个权限

```sql
SELECT COUNT(*) AS has_permission
FROM rbac_user u
JOIN rbac_user_role ur ON u.id = ur.user_id
JOIN rbac_role r ON ur.role_id = r.id
JOIN rbac_role_permission rp ON r.id = rp.role_id
JOIN rbac_permission p ON rp.permission_id = p.id
WHERE u.username = 'admin' 
  AND u.tenant_id = 1 
  AND p.code = 'user:view';
```

### 5. 统计信息

```sql
-- 统计各租户用户数
SELECT t.code, t.name, COUNT(u.id) AS user_count
FROM rbac_tenant t
LEFT JOIN rbac_user u ON t.id = u.tenant_id
GROUP BY t.id;

-- 统计各租户角色数
SELECT t.code, t.name, COUNT(r.id) AS role_count
FROM rbac_tenant t
LEFT JOIN rbac_role r ON t.id = r.tenant_id
GROUP BY t.id;

-- 统计各角色权限数
SELECT r.code, r.name, COUNT(p.id) AS permission_count
FROM rbac_role r
LEFT JOIN rbac_role_permission rp ON r.id = rp.role_id
LEFT JOIN rbac_permission p ON rp.permission_id = p.id
GROUP BY r.id;
```

---

## 修改配置到MySQL

### 1. 添加 MySQL 依赖到 pom.xml

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

### 2. 修改 application.yml

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/rbac?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=utf8mb4
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        format_sql: true
```

**配置说明：**
- `ddl-auto: validate` - 只验证数据库结构，不自动创建表
- `useSSL=false` - 关闭SSL连接（开发环境）
- `serverTimezone=UTC` - 设置服务器时区
- `allowPublicKeyRetrieval=true` - 允许公钥检索

### 3. 重新启动应用

```bash
mvn spring-boot:run -pl rbac-interface
```

---

## 数据备份

### 备份数据库

```bash
mysqldump -u root -p rbac > rbac_backup.sql
```

### 恢复数据库

```bash
mysql -u root -p rbac < rbac_backup.sql
```

---

## 故障排除

### 问题1：连接超时

**症状：** `com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure`

**解决方案：**
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/rbac?allowPublicKeyRetrieval=true&serverTimezone=UTC&useSSL=false&connectTimeout=10000&socketTimeout=10000
```

### 问题2：字符编码错误

**症状：** 中文字符显示为乱码

**解决方案：**
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/rbac?characterEncoding=utf8mb4&useUnicode=true
  jpa:
    database-platform: org.hibernate.dialect.MySQL8Dialect
```

### 问题3：权限不足

**症状：** `Access denied for user 'root'@'localhost'`

**解决方案：** 检查MySQL用户权限
```sql
-- 授予所有权限
GRANT ALL PRIVILEGES ON rbac.* TO 'root'@'localhost' WITH GRANT OPTION;
FLUSH PRIVILEGES;
```

