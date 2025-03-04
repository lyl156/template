package __cronjob_wrapper

import (
	"errors"
	"log/slog"
	"os"
	"sync"

	"github.com/go-co-op/gocron/v2"
)

var (
	once      sync.Once
	scheduler gocron.Scheduler
)

var logger = slog.New(slog.NewJSONHandler(os.Stdout, nil))

// Init must be called before cron jobs can be added. Calling Init multiple times will not have any side effects.
func Init() {
	once.Do(func() {
		s, err := gocron.NewScheduler()
		if err != nil {
			logger.Error("Unable to create a cron scheduler err", err.Error())
			return
		}
		scheduler = s
		scheduler.Start()
	})
}

// AddCronJob creates a cron job with the cron pattern.
// If withSeconds is:
//
//	false: the cron pattern will be * * * * *.
//	true: the cron pattern will be * * * * * *.
//
// fn and parameters are what will be invoked when the cron job is run.
func AddCronJob(pattern string, withSeconds bool, fn any, parameters ...any) error {
	if scheduler == nil {
		return errors.New("scheduler not initialised")
	}

	_, err := scheduler.NewJob(
		gocron.CronJob(pattern, withSeconds),
		gocron.NewTask(fn, parameters...),
	)
	return err
}

// Close should be called for cleaning up of cron jobs.
func Close() {
	if scheduler == nil {
		return
	}
	if err := scheduler.Shutdown(); err != nil {
		logger.Error("Unable to shutdown cron scheduler err", err.Error())
	}
}
