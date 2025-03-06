package _2_choose_strategy

import (
	"context"
	"fmt"
	"log"
	"time"
)

type NotificationType string

const (
	NotificationTypeOnce     NotificationType = "once"
	NotificationTypeInterval NotificationType = "interval"
)

type notifierFn func(ctx context.Context, curTime time.Time) error

type NotifyServiceImpl struct {
	notifiers map[NotificationType]notifierFn
}

func (s *NotifyServiceImpl) registerNotifiers() {
	s.notifiers = map[NotificationType]notifierFn{
		NotificationTypeInterval: s.intervalNotifier(),
		NotificationTypeOnce:     s.onceNotifier(),
	}
}

func (s *NotifyServiceImpl) intervalNotifier() notifierFn {
	return func(ctx context.Context, curTime time.Time) error {
		log.Printf("i am intervalNotifier, time: %+v", curTime)

		return nil
	}
}

func (s *NotifyServiceImpl) onceNotifier() notifierFn {
	return func(ctx context.Context, curTime time.Time) error {
		log.Printf("i am onceNotifier, time: %+v", curTime)

		return nil
	}
}

func (s *NotifyServiceImpl) Notify(ctx context.Context, curTime time.Time) error {
	ntype := NotificationTypeOnce
	notifier, ok := s.notifiers[ntype]
	if !ok {
		return fmt.Errorf("unspported notifierType: %s", ntype)
	}
	if err := notifier(ctx, curTime); err != nil {
		return fmt.Errorf("notify err: %s", err.Error())
	}

	return nil
}

func main() {
	s := &NotifyServiceImpl{}
	s.registerNotifiers()

	s.Notify(context.Background(), time.Now())
}
