package __cronjob_wrapper

type Scheduler interface {
	AddCronJob(spec string, withSeconds bool, cmd any, parameters ...any) error
}

type SchedulerFunc func(spec string, withSeconds bool, cmd any, parameters ...any) error

func (c SchedulerFunc) AddCronJob(spec string, withSeconds bool, cmd any, parameters ...any) error {
	isProduct := false
	if isProduct {
		return nil
	}

	return c(spec, withSeconds, cmd, parameters...)
}
