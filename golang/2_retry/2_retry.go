package __retry

import (
	"context"
	"time"

	"github.com/avast/retry-go"
)

// DoWithData returns the output after a successful attempt. If there are no successful attempts error will be returned.
// Usage:
//
//	rows, err := DoWithData(func() ([]model.SampleData, error) {
//			return dal.ListSampleData(ctx, req)
//	}, DefaultRetryOptionsWithContext(ctx)...)}
func DoWithData[T any](f func() (T, error), options ...retry.Option) (T, error) {
	var res T
	err := retry.Do(func() error {
		var ierr error
		res, ierr = f()
		return ierr
	}, options...)
	return res, err
}

// DefaultRetryOptionsWithContext returns default retry options and takes in context for logging.
// Usage:
//
// retryOptions := DefaultRetryOptionsWithContext(logIDCtx)
func DefaultRetryOptionsWithContext(ctx context.Context) []retry.Option {
	return []retry.Option{
		retry.Attempts(5),
		retry.LastErrorOnly(true),
		retry.MaxJitter(time.Second),
		retry.DelayType(retry.RandomDelay),
		retry.OnRetry(func(n uint, err error) {
			// print err with ctx
			//fmt.Printf("retry attempt: %v, err: %v", n, err)
		}),
	}
}
