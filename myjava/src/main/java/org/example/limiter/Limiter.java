package org.example.limiter;

import java.util.concurrent.CompletableFuture;

public interface Limiter {
    CompletableFuture<Boolean> allowWithLimit(String key, Limit limit);
}