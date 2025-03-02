package org.example.retry;

import static org.junit.jupiter.api.Assertions.*;
import com.github.rholder.retry.*;
import org.junit.jupiter.api.Test;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

class RetryUtilTest {

    @Test
    void testDoWithData_SuccessWithoutRetry() throws Exception {
        // 模拟一个立即成功的操作
        Supplier<String> successfulFunction = () -> "Success";

        // 使用默认的 retry 配置
        Retryer<String> retryer = RetryUtil.defaultRetryOptions();

        // 执行 doWithData，不应该发生重试
        String result = RetryUtil.doWithData(successfulFunction, retryer);

        // 断言结果正确
        assertEquals("Success", result);
    }

    @Test
    void testDoWithData_SuccessAfterRetries() throws Exception {
        AtomicInteger attemptCounter = new AtomicInteger(0);

        // 模拟前 3 次失败，第 4 次成功的操作
        Supplier<String> functionWithRetries = () -> {
            if (attemptCounter.incrementAndGet() < 4) {
                throw new RuntimeException("Simulated Failure");
            }
            return "Success";
        };

        Retryer<String> retryer = RetryUtil.defaultRetryOptions();

        // 执行 doWithData，最终应该返回成功
        String result = RetryUtil.doWithData(functionWithRetries, retryer);

        // 断言最终成功
        assertEquals("Success", result);

        // 断言执行次数是 4（失败 3 次 + 成功 1 次）
        assertEquals(4, attemptCounter.get());
    }

    @Test
    void testDoWithData_FailsAfterMaxRetries() {
        AtomicInteger attemptCounter = new AtomicInteger(0);

        // 这个函数始终失败
        Supplier<String> functionThatAlwaysFails = () -> {
            attemptCounter.incrementAndGet();
            throw new RuntimeException("Simulated Permanent Failure");
        };

        Retryer<String> retryer = RetryUtil.defaultRetryOptions();

        // 断言最终抛出异常
        Exception exception = assertThrows(Exception.class, () -> RetryUtil.doWithData(functionThatAlwaysFails, retryer));

        // 断言异常信息
        assertEquals("Simulated Permanent Failure", exception.getCause().getMessage());

        // 断言方法被调用了 5 次（达到最大重试次数）
        assertEquals(5, attemptCounter.get());
    }

    @Test
    void testDefaultRetryOptions_RetryIfNull() throws Exception {
        AtomicInteger attemptCounter = new AtomicInteger(0);

        // 模拟返回 null 的情况
        Supplier<String> functionReturningNull = () -> {
            if (attemptCounter.incrementAndGet() < 3) {
                return null; // 前两次返回 null，触发重试
            }
            return "Success"; // 第 3 次返回成功
        };

        Retryer<String> retryer = RetryUtil.defaultRetryOptions();

        // 执行方法
        String result = RetryUtil.doWithData(functionReturningNull, retryer);

        // 断言最终返回成功
        assertEquals("Success", result);

        // 断言调用了 3 次
        assertEquals(3, attemptCounter.get());
    }
}
