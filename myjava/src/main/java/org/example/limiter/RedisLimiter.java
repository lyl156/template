package org.example.limiter;

import java.util.concurrent.CompletableFuture;

public class RedisLimiter implements Limiter {
    private final IRateLimit limiter;

    public RedisLimiter(RedisClient rc) {
        this.limiter = new Limit();
    }

    @Override
    public CompletableFuture<Boolean> allowWithLimit(String key, Limit limit) {
        return limiter.allow(key);
    }
}
