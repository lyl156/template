package _6_limiter_factory

import "context"

type IRateLimit interface {
	Allow(ctx context.Context, key string) (bool, error)
}

type Limit struct {
	//redisClient
}

func (l Limit) Allow(ctx context.Context, key string) (bool, error) {
	return true, nil
}
