# 数据库建表语句

## MySQL 5.7+ 建表语句

```sql
-- 租户表
CREATE TABLE rbac_tenant (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '租户ID',
  code VARCHAR(50) NOT NULL UNIQUE COMMENT '租户编码',
  name VARCHAR(100) NOT NULL COMMENT '租户名称',
  enabled BOOLEAN DEFAULT 1 COMMENT '是否启用',
  description VARCHAR(255) COMMENT '描述',
  max_users INT COMMENT '最大用户数',
  created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modified_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  KEY idx_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户表';

-- 用户表
CREATE TABLE rbac_user (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
  username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
  email VARCHAR(100) COMMENT '邮箱',
  password VARCHAR(255) NOT NULL COMMENT '密码（加密）',
  phone VARCHAR(20) COMMENT '电话',
  status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '用户状态：ACTIVE/INACTIVE/LOCKED/DELETED',
  last_login_time TIMESTAMP NULL COMMENT '最后登录时间',
  tenant_id BIGINT NOT NULL COMMENT '租户ID',
  created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modified_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  UNIQUE KEY uk_username (username),
  KEY idx_email (email),
  KEY idx_tenant_id (tenant_id),
  CONSTRAINT fk_user_tenant FOREIGN KEY (tenant_id) REFERENCES rbac_tenant(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 权限表
CREATE TABLE rbac_permission (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '权限ID',
  code VARCHAR(100) NOT NULL UNIQUE COMMENT '权限编码',
  name VARCHAR(100) NOT NULL COMMENT '权限名称',
  description VARCHAR(255) COMMENT '权限描述',
  type VARCHAR(20) NOT NULL COMMENT '权限类型：MENU/BUTTON/API/DATA',
  resource VARCHAR(100) COMMENT '资源名称',
  action VARCHAR(100) COMMENT '操作名称',
  parent_id BIGINT COMMENT '父权限ID',
  sort_order INT COMMENT '排序号',
  tenant_id BIGINT NOT NULL COMMENT '租户ID',
  created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modified_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  UNIQUE KEY uk_code (code),
  KEY idx_tenant_id (tenant_id),
  KEY idx_parent_id (parent_id),
  CONSTRAINT fk_permission_tenant FOREIGN KEY (tenant_id) REFERENCES rbac_tenant(id),
  CONSTRAINT fk_permission_parent FOREIGN KEY (parent_id) REFERENCES rbac_permission(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- 角色表
CREATE TABLE rbac_role (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
  code VARCHAR(50) NOT NULL COMMENT '角色编码',
  name VARCHAR(100) NOT NULL COMMENT '角色名称',
  description VARCHAR(255) COMMENT '角色描述',
  type VARCHAR(20) COMMENT '角色类型：SYSTEM/CUSTOM',
  enabled BOOLEAN DEFAULT 1 COMMENT '是否启用',
  tenant_id BIGINT NOT NULL COMMENT '租户ID',
  created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modified_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  UNIQUE KEY uk_code_tenant (code, tenant_id),
  KEY idx_tenant_id (tenant_id),
  CONSTRAINT fk_role_tenant FOREIGN KEY (tenant_id) REFERENCES rbac_tenant(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 用户-角色关联表
CREATE TABLE rbac_user_role (
  user_id BIGINT NOT NULL COMMENT '用户ID',
  role_id BIGINT NOT NULL COMMENT '角色ID',
  PRIMARY KEY (user_id, role_id),
  KEY idx_role_id (role_id),
  CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES rbac_user(id) ON DELETE CASCADE,
  CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES rbac_role(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 角色-权限关联表
CREATE TABLE rbac_role_permission (
  role_id BIGINT NOT NULL COMMENT '角色ID',
  permission_id BIGINT NOT NULL COMMENT '权限ID',
  PRIMARY KEY (role_id, permission_id),
  KEY idx_permission_id (permission_id),
  CONSTRAINT fk_rp_role FOREIGN KEY (role_id) REFERENCES rbac_role(id) ON DELETE CASCADE,
  CONSTRAINT fk_rp_permission FOREIGN KEY (permission_id) REFERENCES rbac_permission(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';
```


## 初始化示例数据（MySQL）

```sql
-- 插入租户
INSERT INTO rbac_tenant (code, name, enabled, description, max_users) 
VALUES ('default', '默认租户', 1, '系统默认租户', 1000);

-- 插入权限
INSERT INTO rbac_permission (code, name, description, type, resource, action, tenant_id, sort_order) VALUES
('user:view', '查看用户', '查看用户列表和详情', 'API', 'user', 'view', 1, 1),
('user:create', '创建用户', '创建新用户', 'API', 'user', 'create', 1, 2),
('user:edit', '编辑用户', '编辑用户信息', 'API', 'user', 'edit', 1, 3),
('user:delete', '删除用户', '删除用户', 'API', 'user', 'delete', 1, 4),
('role:manage', '管理角色', '管理系统角色', 'API', 'role', 'manage', 1, 5),
('permission:manage', '管理权限', '管理系统权限', 'API', 'permission', 'manage', 1, 6);

-- 插入角色
INSERT INTO rbac_role (code, name, description, type, enabled, tenant_id) VALUES
('admin', '管理员', '系统管理员，拥有所有权限', 'SYSTEM', 1, 1),
('user', '普通用户', '普通用户，拥有基本权限', 'SYSTEM', 1, 1),
('visitor', '访客', '访客，仅查看权限', 'SYSTEM', 1, 1);

-- 关联角色权限 - admin角色
INSERT INTO rbac_role_permission (role_id, permission_id)
SELECT r.id, p.id FROM rbac_role r, rbac_permission p 
WHERE r.code = 'admin' AND p.tenant_id = 1;

-- 关联角色权限 - user角色
INSERT INTO rbac_role_permission (role_id, permission_id)
SELECT r.id, p.id FROM rbac_role r, rbac_permission p 
WHERE r.code = 'user' AND p.code IN ('user:view', 'user:edit');

-- 关联角色权限 - visitor角色
INSERT INTO rbac_role_permission (role_id, permission_id)
SELECT r.id, p.id FROM rbac_role r, rbac_permission p 
WHERE r.code = 'visitor' AND p.code = 'user:view';

-- 插入用户（使用BCrypt加密的密码，密码为admin123和user123）
INSERT INTO rbac_user (username, email, password, phone, status, tenant_id) VALUES
('admin', 'admin@example.com', '$2a$10$SlVZmhKjSZHSVWUaeXW1Be7DlH.PKZbv5H8KnzzVgXXbVxzy2QIDM', '13800138000', 'ACTIVE', 1),
('user', 'user@example.com', '$2a$10$SlVZmhKjSZHSVWUaeXW1Be7DlH.PKZbv5H8KnzzVgXXbVxzy2QIDM', '13800138001', 'ACTIVE', 1),
('visitor', 'visitor@example.com', '$2a$10$SlVZmhKjSZHSVWUaeXW1Be7DlH.PKZbv5H8KnzzVgXXbVxzy2QIDM', '13800138002', 'ACTIVE', 1);

-- 关联用户角色
INSERT INTO rbac_user_role (user_id, role_id) VALUES
((SELECT id FROM rbac_user WHERE username = 'admin'), (SELECT id FROM rbac_role WHERE code = 'admin')),
((SELECT id FROM rbac_user WHERE username = 'user'), (SELECT id FROM rbac_role WHERE code = 'user')),
((SELECT id FROM rbac_user WHERE username = 'visitor'), (SELECT id FROM rbac_role WHERE code = 'visitor'));
```

---

## 表结构说明

### 核心表

| 表名 | 说明 | 关键字段 |
|------|------|---------|
| `rbac_tenant` | 租户表 | code(唯一), name, enabled |
| `rbac_user` | 用户表 | username(唯一), email, password, status |
| `rbac_role` | 角色表 | code+tenantId(唯一), name, enabled |
| `rbac_permission` | 权限表 | code(唯一), name, type, resource, action |

### 关联表

| 表名 | 说明 | 关键说明 |
|------|------|---------|
| `rbac_user_role` | 用户-角色 | 多对多关联 |
| `rbac_role_permission` | 角色-权限 | 多对多关联 |

### 字段类型对应

| 字段类型 | 说明 |
|---------|------|
| BIGINT | 用于ID自增，支持大数据量 |
| VARCHAR | 字符型，根据需求调整长度 |
| BOOLEAN | 布尔型（MySQL用TINYINT(1)） |
| TIMESTAMP | 时间戳，自动记录创建/修改时间 |

---

## 配置MySQL数据源

修改 `application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/rbac?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        format_sql: true
```