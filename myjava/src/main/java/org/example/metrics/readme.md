## Spring Boot Actuator 查看

http://localhost:8080/actuator 看 _links

http://localhost:8080/actuator/env 看依賴的版本等等

http://localhost:8080/actuator/metrics/xx_total
看此 metrics 的打点情况

## Spring Boot 內建指標
| 监控类别   | 指标名称                  | 说明                         |
|----------|------------------------|----------------------------|
| **JVM**  | `jvm.memory.used`      | 已使用的堆内存               |
|          | `jvm.memory.max`       | 最大可用堆内存               |
|          | `jvm.threads.live`     | 活跃线程数                  |
|          | `jvm.gc.pause`         | GC 停顿时间                 |
| **HTTP** | `http.server.requests` | HTTP 请求总数、状态码分布、耗时等 |
| **数据库** | `hikaricp.connections` | HikariCP 连接池的活跃连接数   |
| **CPU & 系统** | `system.cpu.usage`  | 系统 CPU 使用率              |
|          | `process.cpu.usage`    | 进程 CPU 使用率              |
| **自定义** | `xx_total`             | 自定义的 Counter 计数       |


## Trace and traceID
### 使用 Spring Cloud Sleuth 进行 Trace 集成

在 Spring Boot 2.7.18 中，可以使用 **Spring Cloud Sleuth** 进行 Trace 追踪。Sleuth 会自动为 HTTP 请求和日志加上 `traceId`，并在服务间调用时自动透传 `traceId`。
Spring Boot 3 以上使用 OpenTelemetry. 不使用 Spring 則使用 Interceptor 自建。

### **测试自动透传 `traceId`**

可以使用以下 `curl` 命令来测试 `traceId` 是否透传成功：

```sh
curl -X GET "http://localhost:8080/increment" \
     -H "X-B3-TraceId: 123456789abcdef0" \
     -H "X-B3-SpanId: 123456789abcdef0" \
     -H "X-B3-Sampled: 1"
