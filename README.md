# template

## TODO:

### golang

1. mq_wrapper sarama consumer consume message, producer send message
2. 6_7_test

### java

1. mq consumer 沒有 containerFactory 則沒有消費，如何排查
2. metrics 连上 prometheus, 在 grafana 查看
3. 搜寻 ExecutorService, 有多种执行的写法：CompletableFuture.supplyAsync( -> ), executor.submit, CompletionService

####

1. 如果 pom.xml 加入了 slf4j 是不是會覆蓋所有的版本: 是，可以到 external libraries 看 slf4j 跟 logback 的版本
