package org.example.mqwrapper.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageListener.class);
    private final MessageProcessor messageProcessor;

    public KafkaMessageListener(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    @KafkaListener(topics = "learn-topic", groupId = "learn-group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, String> record, Acknowledgment ack) {
        try {
            logger.info("Received message: key={}, value={}, partition={}, offset={}",
                    record.key(), record.value(), record.partition(), record.offset());

            // 处理消息
            boolean success = messageProcessor.process(record.value());

            // 如果成功，手动提交 offset
            if (success) {
                ack.acknowledge();
                logger.info("Offset committed: {}", record.offset());
            } else {
                logger.warn("Processing failed, will retry later.");
            }

        } catch (Exception e) {
            logger.error("Error processing message", e);
        }
    }
}
