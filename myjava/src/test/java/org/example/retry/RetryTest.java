package org.example.retry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {RetryConfig.class, RetryService.class}) // 确保加载 Spring 上下文
@EnableRetry // 启用 Spring Retry
class RetryServiceTest {

    @Autowired
    private RetryService retryService; // 让 Spring 注入带有 @Retryable 的 Bean
//
//    @MockBean
//    private RetryService mockRetryService; // Mock 以控制方法的行为
//
//    @Test
//    void testRetrySuccessAfterFewAttempts() throws Exception {
//        // 计数器用于记录重试次数
//        int[] attemptCounter = {0};
//
//        // Mock 行为：前 2 次抛异常，第 3 次成功
//        doAnswer(invocation -> {
//            attemptCounter[0]++;
//            if (attemptCounter[0] < 3) {
//                throw new RuntimeException("Simulated Failure");
//            }
//            return "Success!";
//        }).when(retryService).doWithData();
//
//        // 断言最终成功
//        assertEquals("Success!", retryService.doWithData());
//
//        // 断言方法调用了 3 次（失败 2 次后成功）
//        assertEquals(3, attemptCounter[0]);
//    }

    @Test
    void testRetryFailureAfterMaxAttempts() {
        long startTime = System.currentTimeMillis(); // 记录开始时间

        // 断言最终抛出了异常
        Exception exception = assertThrows(RuntimeException.class, retryService::doWithData);
        assertEquals("Simulated Failure: Always triggering retry", exception.getMessage());

        long duration = System.currentTimeMillis() - startTime; // 计算时间
        System.out.println("Total Retry Duration: " + duration + " ms");

        // 由于 @Retryable 设置了 5 次重试，且默认 backoff 500ms, maxDelay 1000ms
        // 断言总时间至少是 5 * 500ms = 2500ms
        assertTrue(duration >= 2500, "Retry should take at least 2500ms");
    }
}
