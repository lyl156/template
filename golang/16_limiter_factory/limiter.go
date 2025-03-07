package _6_limiter_factory

import "context"

type Limiter interface {
	AllowWithLimit(ctx context.Context, key string, limit *Limit) (bool, error)
}

type RedisLimiter struct {
	limiter IRateLimit
}

func newRedisLimiter(rc *RedisClient) (Limiter, error) {
	cli := Limit{}

	return &RedisLimiter{
		limiter: cli,
	}, nil
}

func (r *RedisLimiter) AllowWithLimit(ctx context.Context, key string, limit *Limit) (bool, error) {
	allow, err := r.limiter.Allow(ctx, key) //WithLimit(PerMinute(1)),

	if err != nil {
		return true, err
	}

	return allow, nil
}

type MockLimiter struct {
}

func newMockLimiter() (Limiter, error) {
	return &MockLimiter{}, nil
}

func (m *MockLimiter) AllowWithLimit(ctx context.Context, key string, limit *Limit) (bool, error) {
	return true, nil
}
