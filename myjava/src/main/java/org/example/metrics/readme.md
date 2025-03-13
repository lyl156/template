http://localhost:8080/actuator 看 _links

http://localhost:8080/actuator/metrics/xx_total
看此 metrics 的打点情况



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
