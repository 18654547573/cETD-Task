# eCTD 4.0 Application Management Backend

基于 Spring Boot + MyBatis + MySQL 的 eCTD 4.0 应用管理后端系统。

## 项目概述

本项目实现了 eCTD 4.0 标准的应用程序（Application）和提交单元（Submission Unit）管理功能，支持：

- eCTD Application 的创建、查询、更新、删除
- Submission Unit 的创建、查询、更新、删除
- Root Section 树形结构管理
- Context of Use (CoU) 数据管理

## 技术栈

- **Java**: 17
- **Spring Boot**: 3.1.0
- **MyBatis**: 3.0.3
- **MySQL**: 8.0+
- **Maven**: 3.6+

## 项目结构

```
ectd-backend/
├── src/
│   ├── main/
│   │   ├── java/com/ectd/backend/
│   │   │   ├── model/           # 实体类
│   │   │   ├── mapper/          # MyBatis Mapper 接口
│   │   │   ├── service/         # 业务逻辑层
│   │   │   ├── controller/      # REST API 控制器
│   │   │   ├── config/          # 配置类
│   │   │   └── EctdBackendApplication.java
│   │   └── resources/
│   │       ├── mapper/          # MyBatis XML 映射文件
│   │       ├── application.yml  # 应用配置
│   │       └── schema.sql       # 数据库建表脚本
│   └── test/
└── pom.xml
```

## 数据库设计

### 核心表结构

1. **ectd_application**: 存储 eCTD 应用程序信息
   - `app_id`: 主键
   - `app_number`: 应用编号（如 NDA-202501）
   - `app_type`: 应用类型（NDA, BLA, ANDA）
   - `root_section`: 根节点树形结构（JSON）
   - `status`: 状态

2. **ectd_submission_unit**: 存储提交单元信息
   - `su_id`: 主键
   - `app_id`: 关联的应用ID
   - `sequence_num`: 序列号
   - `effective_date`: 生效日期
   - `su_type`: 提交类型
   - `cou_data`: CoU数据（JSON）
   - `status`: 状态

## API 接口

### Application APIs

- `POST /api/applications` - 创建新应用
- `GET /api/applications` - 获取所有应用
- `GET /api/applications/{id}` - 根据ID获取应用
- `GET /api/applications/number/{number}` - 根据编号获取应用
- `PUT /api/applications/{id}` - 更新应用
- `PUT /api/applications/{id}/root-section` - 更新根节点结构
- `DELETE /api/applications/{id}` - 删除应用

### Submission Unit APIs

- `POST /api/submission-units` - 创建新提交单元
- `GET /api/submission-units` - 获取所有提交单元
- `GET /api/submission-units/{id}` - 根据ID获取提交单元
- `GET /api/submission-units/by-app/{appId}` - 获取应用的所有提交单元
- `PUT /api/submission-units/{id}` - 更新提交单元
- `PUT /api/submission-units/{id}/cou-data` - 更新CoU数据
- `DELETE /api/submission-units/{id}` - 删除提交单元

## 快速开始

### 1. 环境准备

- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 2. 数据库配置

1. 创建数据库：
```sql
CREATE DATABASE ectd_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行建表脚本：
```bash
mysql -u root -p ectd_db < src/main/resources/schema.sql
```

3. 修改 `application.yml` 中的数据库连接配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ectd_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 3. 运行应用

```bash
# 编译项目
mvn clean compile

# 运行应用
mvn spring-boot:run
```

应用将在 `http://localhost:8080` 启动。

### 4. 测试 API

创建新应用：
```bash
curl -X POST http://localhost:8080/api/applications \
  -H "Content-Type: application/json" \
  -d '{"appNumber": "NDA-202503", "appType": "NDA"}'
```

获取所有应用：
```bash
curl http://localhost:8080/api/applications
```

## 开发说明

### JSON 数据结构

#### Root Section 示例
```json
{
  "id": 9999990,
  "nodeType": "application",
  "name": "NDA-202501",
  "children": [
    {
      "id": 8154,
      "nodeType": "module",
      "name": "1. Administrative information",
      "children": []
    },
    {
      "id": 8155,
      "nodeType": "module", 
      "name": "2. Overview and Summaries",
      "children": []
    }
  ]
}
```

#### CoU Data 示例
```json
{
  "operations": [
    {
      "type": "add",
      "nodeId": 8154,
      "documentPath": "/m1/admin-info.pdf"
    }
  ]
}
```

## 部署

### 打包应用
```bash
mvn clean package -DskipTests
```

### 运行 JAR 文件
```bash
java -jar target/ectd-backend-1.0.0.jar
```

## 注意事项

1. 确保 MySQL 版本支持 JSON 数据类型（5.7+）
2. 生产环境请修改默认的数据库密码
3. 建议配置连接池参数以优化性能
4. 可根据实际需求调整日志级别

## 许可证

本项目仅用于面试演示目的。

