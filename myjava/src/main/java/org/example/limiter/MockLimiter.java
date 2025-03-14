package org.example.limiter;

import java.util.concurrent.CompletableFuture;

public class MockLimiter implements Limiter {
    @Override
    public CompletableFuture<Boolean> allowWithLimit(String key, Limit limit) {
        return CompletableFuture.completedFuture(true);
    }
}
