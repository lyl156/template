package org.example.mqwrapper;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.example.mqwrapper.producer.KafkaProducerService;


@SpringBootApplication
public class KafkaApplication implements CommandLineRunner {

    private final KafkaProducerService kafkaProducerService;

    public KafkaApplication(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    public static void main(String[] args) {
        SpringApplication.run(KafkaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        kafkaProducerService.sendMessage("learn-topic", "Hello Kafka from Spring Boot!");

        System.out.println( "after send message" );
//        KafkaConsumerService 由 @KafkaListener 自动消费
    }
}
