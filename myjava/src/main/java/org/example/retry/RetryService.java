package org.example.retry;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class RetryService {

    /**
     * Executes the given function with retry logic.
     * If all attempts fail, it will throw an exception.
     *
     * Usage:
     * List<SampleData> rows = retryService.doWithData(() -> dal.listSampleData(req));
     */
    @Retryable(value = Exception.class, maxAttempts = 5, backoff = @Backoff(delay = 500, maxDelay = 1000, multiplier = 2))
    public String doWithData() throws Exception {
        // 这里模拟始终失败，导致 retry
        throw new RuntimeException("Simulated Failure: Always triggering retry");
    }
}
