# 旅行计划超级智能体

基于 Spring AI、RAG、Tool Calling 和 MCP 的旅行规划 AI Agent。
支持多轮对话、知识检索增强、工具调用与流式输出，可用于生成可执行的旅行计划。

## 项目亮点

- 多轮对话记忆：基于 Spring AI Chat Memory 维持上下文
- ReAct 智能体：支持自主规划与工具链调用
- RAG 检索增强：支持查询重写和知识库问答能力
- MCP 集成：可接入高德地图与自定义 MCP Server
- 流式响应：前端通过 SSE 实时展示生成过程

## 技术栈

- 后端：Java 21、Spring Boot 3、Spring AI、Maven
- 前端：Vue 3、Vite、Axios
- 可选能力：PostgreSQL + PGVector、MCP（stdio / sse）

## 项目结构

```text
yu-ai-agent-master/
├─ src/                         # 后端主服务（travel-plan-ai-agent）
├─ travel-ai-agent-frontend/    # 前端项目（Vue）
├─ yu-image-search-mcp-server/  # 独立 MCP Server 示例
├─ Dockerfile
└─ README.md
```

## 快速开始

### 1) 启动后端

先在 `src/main/resources/application.yml` 中配置必要密钥：

- `spring.ai.dashscope.api-key`
- `search-api.api-key`

然后在项目根目录运行：

```powershell
.\mvnw.cmd spring-boot:run
```

默认访问地址：

- 服务基址：`http://localhost:8123/api`
- 健康检查：`http://localhost:8123/api/health`
- Swagger：`http://localhost:8123/api/swagger-ui.html`

### 2) 启动前端

```powershell
Set-Location "travel-ai-agent-frontend"
npm install
npm run dev
```

前端默认端口：`5173`

前端当前默认请求后端地址为：`http://localhost:8123/api`（见 `travel-ai-agent-frontend/src/api/http.js`）。

### 3) 体验智能体接口（示例）

Manus 超级智能体（SSE）：

```text
GET /api/ai/manus/chat?message=帮我规划一个5天东京自由行，预算1万元
```

其他对话接口：

- `GET /api/ai/love_app/chat/sync`
- `GET /api/ai/love_app/chat/sse`
- `GET /api/ai/love_app/chat/server_sent_event`
- `GET /api/ai/love_app/chat/sse_emitter`

## MCP 与扩展说明

项目已预留 MCP 能力：

- 主服务配置文件中 `spring.ai.mcp.client` 目前默认注释
- `src/main/resources/mcp-servers.json` 中提供了 MCP Server 配置样例（如高德、图片搜索）
- `yu-image-search-mcp-server` 提供了独立 MCP Server 示例工程

如需启用 MCP：

1. 启动并配置目标 MCP Server
2. 在主服务 `application.yml` 中开启对应 `spring.ai.mcp.client` 配置
3. 按需修改 `mcp-servers.json` 的命令、路径与环境变量

## 内置工具能力

当前工具注册位于 `src/main/java/com/yupi/yuaiagent/tools/ToolRegistration.java`，包含：

- 文件操作
- 网页搜索
- 网页抓取
- 资源下载
- 终端操作
- PDF 生成
- 任务终止

## 常见问题

- 密钥未配置：请优先检查 `application.yml` 中两个 API Key 是否已填写
- 前端无法连通后端：确认后端运行在 `8123` 端口且上下文路径为 `/api`
- SSE 中断：先检查后端日志，再确认网络代理或跨域策略
- MCP 不生效：确认已启动 MCP Server，且主服务已开启 MCP Client 配置

## 截图

![项目界面](travel-ai-agent-frontend/Screenshot%202026-03-16%20144640.jpg)
