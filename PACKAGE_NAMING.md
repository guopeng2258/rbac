# 包命名规范说明

## ✅ 已完成重构

项目已从 `interface_` 重构为标准的 `web` 包名。

### 重构前后对比

| 项目 | 重构前 | 重构后 |
|------|--------|--------|
| 包名 | `com.example.rbac.interface_` | `com.example.rbac.web` |
| 目录 | `rbac-interface/src/main/java/com/example/rbac/interface_/` | `rbac-interface/src/main/java/com/example/rbac/web/` |
| 原因 | 避免Java关键字冲突 | 使用业界标准命名 |

---

## 📦 当前项目包结构

```
com.example.rbac/
├── core/                          # 核心领域层
│   └── domain/
│       ├── model/                 # 聚合根和实体
│       ├── service/               # 领域服务
│       ├── repository/            # 仓储接口
│       └── event/                 # 领域事件
│
├── infrastructure/                # 基础设施层
│   ├── persistence/
│   │   ├── jpa/                   # JPA实体和仓储
│   │   ├── mapper/                # 对象映射器
│   │   └── *RepositoryImpl        # 仓储实现
│   ├── event/                     # 事件发布实现
│   └── cache/                     # 缓存实现
│
├── application/                   # 应用服务层
│   ├── service/                   # 应用服务
│   ├── command/                   # 命令对象
│   ├── dto/                       # 数据传输对象
│   └── assembler/                 # 数据组装器
│
└── web/                          # Web层（原interface_）✨
    ├── controller/               # REST控制器
    ├── security/                 # 安全认证
    ├── aop/                      # AOP切面
    └── config/                   # 配置类
```

---

## 🎯 为什么选择 `web` 而不是其他名称？

### 业界常见命名对比

| 包名 | 使用场景 | 优点 | 缺点 |
|------|---------|------|------|
| **`web`** ✅ | Spring Boot项目 | 简洁、业界标准、Spring官方推荐 | - |
| `api` | RESTful API项目 | 强调API风格 | 不适合包含页面的项目 |
| `adapter` | DDD严格派 | 符合六边形架构 | 较抽象，新人理解成本高 |
| `controller` | 小型项目 | 直观 | 只能放控制器，不适合大项目 |
| `presentation` | 传统企业应用 | 学术性强 | 名称过长 |
| `interface_` ❌ | 避免关键字冲突 | 避免编译错误 | 不规范，带下划线不美观 |

### Spring Boot 官方推荐结构

Spring Boot官方文档推荐的包结构：
```
com.example.myapp/
├── domain/
├── service/
├── repository/
└── web/          ← Spring官方推荐
    ├── controller/
    └── rest/
```

参考：[Spring Boot Reference - Structuring Your Code](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.structuring-your-code)

---

## 📚 其他知名开源项目的命名

| 项目 | Web层包名 | 说明 |
|------|----------|------|
| Spring PetClinic | `web` | Spring官方示例项目 |
| JHipster | `web.rest` | 企业级应用生成器 |
| Alibaba Nacos | `controller` | 阿里巴巴开源项目 |
| Apache SkyWalking | `webapp` | APM系统 |

---

## 🔄 如果需要切换到其他命名

### 切换到 `api`（适合纯API项目）

```bash
cd /Users/perry.guo/project/rbac/rbac-interface/src/main/java/com/example/rbac
mv web api

# 批量替换包名
find ./rbac-interface -type f -name "*.java" -exec sed -i '' 's/com\.example\.rbac\.web/com.example.rbac.api/g' {} \;
```

### 切换到 `adapter`（DDD严格派）

```bash
cd /Users/perry.guo/project/rbac/rbac-interface/src/main/java/com/example/rbac
mv web adapter

# 批量替换包名
find ./rbac-interface -type f -name "*.java" -exec sed -i '' 's/com\.example\.rbac\.web/com.example.rbac.adapter/g' {} \;
```

---

## ✅ 验证重构结果

### 1. 编译测试
```bash
mvn clean compile -DskipTests
```

### 2. 运行测试
```bash
mvn test
```

### 3. 启动应用
```bash
mvn spring-boot:run -pl rbac-interface
```

---

## 📝 注意事项

1. **IDE自动导入**：确保IDE的自动导入功能正确识别新包名
2. **Git提交**：重构后记得提交代码
3. **文档更新**：相关文档已同步更新
4. **团队沟通**：通知团队成员包名变更

---

## 🎓 DDD分层架构术语对照

| 层次 | 英文术语 | 常用包名 | 本项目 |
|------|---------|---------|--------|
| 表现层 | Presentation Layer | `web`, `api`, `adapter` | `web` ✅ |
| 应用层 | Application Layer | `application`, `service` | `application` |
| 领域层 | Domain Layer | `domain`, `core` | `core` |
| 基础设施层 | Infrastructure Layer | `infrastructure`, `infra` | `infrastructure` |

---

## 📖 参考资料

- [Spring Boot官方文档 - 代码结构](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.structuring-your-code)
- [DDD Reference - Eric Evans](https://www.domainlanguage.com/ddd/reference/)
- [Hexagonal Architecture - Alistair Cockburn](https://alistair.cockburn.us/hexagonal-architecture/)
- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

---

## 总结

✅ **当前使用：`web`**
- 符合Spring Boot最佳实践
- 简洁、专业、易理解
- 业界广泛使用
- 适合企业级项目

重构完成，编译通过！🎉

