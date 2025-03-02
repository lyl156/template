package org.example.retry;

import com.github.rholder.retry.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class RetryUtil {

    /**
     * Executes the given function with retry logic.
     * If all attempts fail, it will return the last error.
     *
     * Usage:
     * List<SampleData> rows = RetryUtil.doWithData(() -> dal.listSampleData(req), RetryUtil.defaultRetryOptions());
     */
    public static <T> T doWithData(Supplier<T> function, Retryer<T> retryer) throws Exception {
        return retryer.call(function::get);
    }

    /**
     * Provides default retry options.
     *
     * Usage:
     * Retryer<String> retryer = RetryUtil.defaultRetryOptions();
     */
    public static <T> Retryer<T> defaultRetryOptions() {
        return RetryerBuilder.<T>newBuilder()
                .retryIfException()
                .retryIfResult(result -> result == null) // 如果返回结果为 null，也重试
                .withWaitStrategy(WaitStrategies.randomWait(500, TimeUnit.MILLISECONDS, 1000, TimeUnit.MILLISECONDS)) // 随机等待时间
                .withStopStrategy(StopStrategies.stopAfterAttempt(5)) // 最多重试 5 次
                .withRetryListener(new RetryListener() { // 这里改成显式的匿名类
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        if (attempt.hasException()) {
                            System.out.println("Retry attempt: " + attempt.getAttemptNumber() + ", error: " + attempt.getExceptionCause());
                        }
                    }
                })
                .build();
    }


    public static void main(String[] args) {
        try {
            String result = doWithData(() -> {
                if (Math.random() > 0.8) {
                    return "Success!";
                } else {
                    throw new RuntimeException("Simulated Failure");
                }
            }, defaultRetryOptions());

            System.out.println("Final Result: " + result);
        } catch (Exception e) {
            System.err.println("Retry failed: " + e.getMessage());
        }
    }
}
