package _6_limiter_factory

type RedisClient struct {
}

var GlobalFactory *Factory

func MustInit() error {
	//rc := GetRedisClient()
	f := &Factory{ /*rc*/ }

	GlobalFactory = f

	return nil
}

type Factory struct {
	rc *RedisClient
}

func (l *Factory) GenLimiter(useRedis bool) (Limiter, error) {
	if useRedis {
		return newRedisLimiter(l.rc)
	}

	return newMockLimiter()
}
