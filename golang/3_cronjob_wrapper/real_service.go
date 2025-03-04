package __cronjob_wrapper

import (
	"context"
	"fmt"
	"time"
)

type cServiceImpl struct {
	cronScheduler Scheduler
}

func NewService(cronScheduler Scheduler) *cServiceImpl {
	cs := &cServiceImpl{
		cronScheduler: cronScheduler,
	}

	if err := cs.registerSpecificJob(); err != nil {
		fmt.Printf("unable to registerSpecificJob, err: %s", err.Error())
	}

	return cs
}

func (c *cServiceImpl) registerSpecificJob() error {
	err := c.cronScheduler.AddCronJob("* * * * *", false, func() {
		logIDCtx := context.WithValue(context.Background(), "logKey", "realLogKey")
		ctx, cancelFn := context.WithTimeout(logIDCtx, time.Second*time.Duration(60*2)) // Give 2 x (lock TTL) buffer before we timeout.

		defer func(startTime time.Time) {
			// make metrics of start time

			cancelFn()
		}(time.Now())

		c.doSpecificJob(ctx)
	})

	return err
}

func (c *cServiceImpl) doSpecificJob(ctx context.Context) {
	// do specific job
}
