package org.example.limiter;

import java.util.concurrent.CompletableFuture;

public interface IRateLimit {
    CompletableFuture<Boolean> allow(String key);
}