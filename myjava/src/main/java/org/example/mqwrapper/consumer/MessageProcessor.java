package org.example.mqwrapper.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MessageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MessageProcessor.class);

    public boolean process(String message) {
        try {
            logger.info("Processing message: {}", message);
            // 业务逻辑
            return true;
        } catch (Exception e) {
            logger.error("Error processing message", e);
            return false;
        }
    }
}