package org.example.retry;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RetryConfig {
//
//    @Bean
//    public RetryTemplate retryTemplate() {
//        return RetryTemplate.builder()
//                .maxAttempts(5)
//                .fixedBackoff(500)
//                .retryOn(RuntimeException.class)
//                .build();
//    }
}
