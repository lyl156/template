package __cache_wrapper

import (
	"context"
	"fmt"
	"time"

	"github.com/lyl156/template/4_cache_wrapper/local"
)

func main() {
	cache := local.NewLRUCache(time.Hour, 10000)
	ctx := context.Background()

	e, ok := cache.Get(ctx, "key")
	if ok {
		fmt.Println(e)
		return
	}

	// get value and set in cache
	// get value
	cache.Set(ctx, "key", "value", time.Hour)
}
