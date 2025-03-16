package org.example.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

import org.slf4j.MDC;


@SpringBootApplication
public class MetricsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetricsApplication.class, args);
    }

    @Bean
    public Counter xxFailureCounter(MeterRegistry registry) {
        return Counter.builder("xx_total")
                .description("Count of xx usage in yy process")
                .tag("status", "failure")
                .register(registry);
    }
}

@RestController
class MetricsController {
    private static final Logger logger = LoggerFactory.getLogger(MetricsController.class);
    private final Counter xxFailureCounter;

    public MetricsController(Counter xxFailureCounter) {
        this.xxFailureCounter = xxFailureCounter;
    }

    @GetMapping("/increment")
    public String incrementMetric(@RequestParam(defaultValue = "failure") String status) {
        xxFailureCounter.increment();

        String traceId = MDC.get("traceId");
        logger.info("当前 Trace ID:{}", traceId);

        return "Metric incremented for status: " + status;
    }
}


@Component
class ConfigChecker {

    @Value("${management.endpoints.web.exposure.include:NOT_FOUND}")
    private String endpoint;

    @PostConstruct
    public void checkConfig() {
        System.out.println("management.endpoints.web.exposure.include = " + endpoint);
    }
}

