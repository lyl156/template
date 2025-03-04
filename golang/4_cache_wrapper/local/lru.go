package local

import (
	"context"
	"time"

	"github.com/bluele/gcache"
)

type lruCache struct {
	cache gcache.Cache
}

const (
	// NoExpiration is approximately 290 years.
	NoExpiration time.Duration = time.Hour * 43800
)

func NewLRUCache(ttl time.Duration, size int) Cache {
	return &lruCache{
		cache: gcache.New(size).
			LRU().
			Expiration(ttl).
			Build(),
	}
}

func (c *lruCache) Get(_ context.Context, key string) (interface{}, bool) {
	if v, err := c.cache.Get(key); err == nil {
		return v, true
	}

	return nil, false
}

func (c *lruCache) Set(_ context.Context, key string, value interface{}, expired time.Duration) {
	c.cache.SetWithExpire(key, value, expired)
}

func (c *lruCache) GetWithFn(_ context.Context, key string, fn func() interface{}, expired time.Duration) (interface{}, bool) {
	if v, err := c.cache.Get(key); err == nil {
		return v, true
	}
	value := fn()
	c.cache.Set(key, value)
	return value, false
}
