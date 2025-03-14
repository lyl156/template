package org.example.limiter;

import java.util.concurrent.CompletableFuture;

public class Limit implements IRateLimit {
    @Override
    public CompletableFuture<Boolean> allow(String key) {
        return CompletableFuture.completedFuture(true);
    }
}