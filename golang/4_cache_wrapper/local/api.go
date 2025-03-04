package local

import (
	"context"
	"time"
)

type Cache interface {
	Get(ctx context.Context, key string) (interface{}, bool)
	GetWithFn(ctx context.Context, key string, fn func() interface{}, expired time.Duration) (interface{}, bool)
	Set(ctx context.Context, key string, value interface{}, expired time.Duration)
}
