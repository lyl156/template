package org.example.mqwrapper.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.apache.kafka.clients.producer.RecordMetadata;


@Service
public class KafkaProducerService {

//    @Autowired 不能注入 final 变量，因为 final 变量 必须在声明时或构造函数中初始化，
//    但 @Autowired 是 运行时注入，Spring 无法修改 final 变量的值。
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String message) {
        try {
            RecordMetadata metadata = kafkaTemplate.send(topic, message).get().getRecordMetadata();
            System.out.printf("✅ 成功发送消息到 Kafka: Topic=%s, Partition=%d, Offset=%d%n",
                    metadata.topic(), metadata.partition(), metadata.offset());
        } catch (Exception e) {
            System.err.println("❌ 发送消息失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendKeyedMessage(String topic, String key, String message) {
        kafkaTemplate.send(topic, key, message);
    }

    @Transactional
    public void sendTransactionalMessage(String topic, String message1, String message2) {
        kafkaTemplate.executeInTransaction(operations -> {
            operations.send(topic, message1);
            operations.send(topic, message2);
            return true;
        });
    }

    public void sendSyncMessage(String topic, String message) {
        try {
            kafkaTemplate.send(topic, message).get();
            System.out.println("Message sent successfully!");
        } catch (Exception e) {
            System.err.println("Failed to send message: " + e.getMessage());
        }
    }

//    public void sendAsyncMessage(String topic, String message) {
//        kafkaTemplate.send(topic, message).whenComplete((result, ex) -> {
//            if (ex == null) {
//                System.out.println("Message sent successfully! Partition: " + result.getRecordMetadata().partition());
//            } else {
//                System.err.println("Failed to send message: " + ex.getMessage());
//            }
//        });
//    }
}
