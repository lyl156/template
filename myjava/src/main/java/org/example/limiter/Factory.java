package org.example.limiter;

public class Factory {
    private final RedisClient rc;

    public Factory() {
        this.rc = new RedisClient();
    }

    public Limiter genLimiter(boolean useRedis) {
        if (useRedis) {
            return new RedisLimiter(rc);
        }
        return new MockLimiter();
    }
}
