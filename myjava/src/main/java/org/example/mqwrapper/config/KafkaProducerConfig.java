package org.example.mqwrapper.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.*;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

// ProducerFactory<String, String>	负责 创建 Kafka 生产者，管理 Kafka 连接
// KafkaTemplate<String, String>	负责 发送 Kafka 消息，提供 事务、重试、异步回调
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all"); // 确保消息不丢失
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3); // 失败重试

        // 重要：开启事务
        // 在 application.properties 內，spring.kafka.producer.transaction-id-prefix=kafka-tx-
//        configProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "kafka-tx-"); // 事务 ID 前缀

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
//        KafkaTemplate<String, String> template = new KafkaTemplate<>(producerFactory());
//        template.setTransactionIdPrefix("kafka-tx-"); // 事务 ID 前缀

        return new KafkaTemplate<>(producerFactory());

    }
}
