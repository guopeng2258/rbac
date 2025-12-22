-- MySQL 完整初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS rbac DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE rbac;

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

-- ============================================
-- 插入初始化数据
-- ============================================

-- 插入租户
INSERT INTO rbac_tenant (code, name, enabled, description, max_users) 
VALUES 
('tenant_001', '默认租户', 1, '系统默认租户', 1000),
('tenant_002', '测试租户', 1, '测试用租户', 500);

-- 插入权限
INSERT INTO rbac_permission (code, name, description, type, resource, action, tenant_id, sort_order) VALUES
-- 用户管理权限
('user:view', '查看用户', '查看用户列表和详情', 'API', 'user', 'view', 1, 1),
('user:create', '创建用户', '创建新用户', 'API', 'user', 'create', 1, 2),
('user:edit', '编辑用户', '编辑用户信息', 'API', 'user', 'edit', 1, 3),
('user:delete', '删除用户', '删除用户', 'API', 'user', 'delete', 1, 4),

-- 角色管理权限
('role:view', '查看角色', '查看角色列表', 'API', 'role', 'view', 1, 5),
('role:create', '创建角色', '创建新角色', 'API', 'role', 'create', 1, 6),
('role:edit', '编辑角色', '编辑角色信息', 'API', 'role', 'edit', 1, 7),
('role:delete', '删除角色', '删除角色', 'API', 'role', 'delete', 1, 8),

-- 权限管理权限
('permission:view', '查看权限', '查看权限列表', 'API', 'permission', 'view', 1, 9),
('permission:manage', '管理权限', '管理系统权限', 'API', 'permission', 'manage', 1, 10),

-- 菜单权限
('menu:user', '用户管理菜单', '用户管理菜单', 'MENU', 'menu', 'user', 1, 11),
('menu:role', '角色管理菜单', '角色管理菜单', 'MENU', 'menu', 'role', 1, 12),
('menu:permission', '权限管理菜单', '权限管理菜单', 'MENU', 'menu', 'permission', 1, 13);

-- 为租户2插入权限
INSERT INTO rbac_permission (code, name, description, type, resource, action, tenant_id, sort_order) VALUES
('user:view', '查看用户', '查看用户列表和详情', 'API', 'user', 'view', 2, 1),
('user:create', '创建用户', '创建新用户', 'API', 'user', 'create', 2, 2);

-- 插入角色
INSERT INTO rbac_role (code, name, description, type, enabled, tenant_id) VALUES
-- 租户1的角色
('admin', '管理员', '系统管理员，拥有所有权限', 'SYSTEM', 1, 1),
('user', '普通用户', '普通用户，拥有基本权限', 'SYSTEM', 1, 1),
('visitor', '访客', '访客，仅查看权限', 'SYSTEM', 1, 1),

-- 租户2的角色
('admin', '管理员', '系统管理员', 'SYSTEM', 1, 2),
('user', '普通用户', '普通用户', 'SYSTEM', 1, 2);

-- 关联角色权限 - 租户1 admin角色拥有所有权限
INSERT INTO rbac_role_permission (role_id, permission_id)
SELECT r.id, p.id FROM rbac_role r, rbac_permission p 
WHERE r.code = 'admin' AND r.tenant_id = 1 AND p.tenant_id = 1;

-- 关联角色权限 - 租户1 user角色
INSERT INTO rbac_role_permission (role_id, permission_id)
SELECT r.id, p.id FROM rbac_role r, rbac_permission p 
WHERE r.code = 'user' AND r.tenant_id = 1 AND p.tenant_id = 1 
  AND p.code IN ('user:view', 'user:edit', 'menu:user');

-- 关联角色权限 - 租户1 visitor角色
INSERT INTO rbac_role_permission (role_id, permission_id)
SELECT r.id, p.id FROM rbac_role r, rbac_permission p 
WHERE r.code = 'visitor' AND r.tenant_id = 1 AND p.tenant_id = 1
  AND p.code IN ('user:view', 'menu:user');

-- 关联角色权限 - 租户2 admin角色
INSERT INTO rbac_role_permission (role_id, permission_id)
SELECT r.id, p.id FROM rbac_role r, rbac_permission p 
WHERE r.code = 'admin' AND r.tenant_id = 2 AND p.tenant_id = 2;

-- 关联角色权限 - 租户2 user角色
INSERT INTO rbac_role_permission (role_id, permission_id)
SELECT r.id, p.id FROM rbac_role r, rbac_permission p 
WHERE r.code = 'user' AND r.tenant_id = 2 AND p.tenant_id = 2
  AND p.code = 'user:view';

-- 插入用户（使用BCrypt加密的密码）
-- admin123 的BCrypt哈希值：$2a$10$SlVZmhKjSZHSVWUaeXW1Be7DlH.PKZbv5H8KnzzVgXXbVxzy2QIDM
-- user123 的BCrypt哈希值：$2a$10$SlVZmhKjSZHSVWUaeXW1Be7DlH.PKZbv5H8KnzzVgXXbVxzy2QIDM
INSERT INTO rbac_user (username, email, password, phone, status, tenant_id) VALUES
-- 租户1用户
('admin', 'admin@example.com', '$2a$10$SlVZmhKjSZHSVWUaeXW1Be7DlH.PKZbv5H8KnzzVgXXbVxzy2QIDM', '13800138000', 'ACTIVE', 1),
('user01', 'user01@example.com', '$2a$10$SlVZmhKjSZHSVWUaeXW1Be7DlH.PKZbv5H8KnzzVgXXbVxzy2QIDM', '13800138001', 'ACTIVE', 1),
('visitor', 'visitor@example.com', '$2a$10$SlVZmhKjSZHSVWUaeXW1Be7DlH.PKZbv5H8KnzzVgXXbVxzy2QIDM', '13800138002', 'ACTIVE', 1),

-- 租户2用户
('admin', 'admin2@example.com', '$2a$10$SlVZmhKjSZHSVWUaeXW1Be7DlH.PKZbv5H8KnzzVgXXbVxzy2QIDM', '13800238000', 'ACTIVE', 2),
('user02', 'user02@example.com', '$2a$10$SlVZmhKjSZHSVWUaeXW1Be7DlH.PKZbv5H8KnzzVgXXbVxzy2QIDM', '13800238001', 'ACTIVE', 2);

-- 关联用户角色 - 租户1
INSERT INTO rbac_user_role (user_id, role_id)
SELECT u.id, r.id FROM rbac_user u, rbac_role r 
WHERE u.username = 'admin' AND u.tenant_id = 1 AND r.code = 'admin' AND r.tenant_id = 1;

INSERT INTO rbac_user_role (user_id, role_id)
SELECT u.id, r.id FROM rbac_user u, rbac_role r 
WHERE u.username = 'user01' AND u.tenant_id = 1 AND r.code = 'user' AND r.tenant_id = 1;

INSERT INTO rbac_user_role (user_id, role_id)
SELECT u.id, r.id FROM rbac_user u, rbac_role r 
WHERE u.username = 'visitor' AND u.tenant_id = 1 AND r.code = 'visitor' AND r.tenant_id = 1;

-- 关联用户角色 - 租户2
INSERT INTO rbac_user_role (user_id, role_id)
SELECT u.id, r.id FROM rbac_user u, rbac_role r 
WHERE u.username = 'admin' AND u.tenant_id = 2 AND r.code = 'admin' AND r.tenant_id = 2;

INSERT INTO rbac_user_role (user_id, role_id)
SELECT u.id, r.id FROM rbac_user u, rbac_role r 
WHERE u.username = 'user02' AND u.tenant_id = 2 AND r.code = 'user' AND r.tenant_id = 2;

-- ============================================
-- 验证数据
-- ============================================

-- 查看租户
SELECT '===== 租户信息 =====' AS info;
SELECT id, code, name, enabled FROM rbac_tenant;

-- 查看用户
SELECT '===== 用户信息 =====' AS info;
SELECT id, username, email, status, tenant_id FROM rbac_user;

-- 查看角色
SELECT '===== 角色信息 =====' AS info;
SELECT id, code, name, tenant_id FROM rbac_role;

-- 查看权限
SELECT '===== 权限信息 =====' AS info;
SELECT id, code, name, type, tenant_id FROM rbac_permission;

-- 查看用户角色关联
SELECT '===== 用户角色关联 =====' AS info;
SELECT u.username, r.code FROM rbac_user_role ur
JOIN rbac_user u ON ur.user_id = u.id
JOIN rbac_role r ON ur.role_id = r.id;

-- 查看角色权限关联
SELECT '===== 角色权限关联 =====' AS info;
SELECT r.code AS role_code, p.code AS permission_code FROM rbac_role_permission rp
JOIN rbac_role r ON rp.role_id = r.id
JOIN rbac_permission p ON rp.permission_id = p.id
WHERE r.tenant_id = 1 LIMIT 10;

