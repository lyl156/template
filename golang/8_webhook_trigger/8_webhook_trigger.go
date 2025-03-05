package __webhook_trigger

import (
	"context"
	"fmt"
	"log"
	"time"

	"golang.org/x/sync/errgroup"
)

type Trigger struct {
	option    *Option
	user      string
	messageID string
}

func NewTrigger(user string, messageID string, opts ...Options) *Trigger {
	curOption := &Option{}
	for _, optF := range opts {
		optF(curOption)
	}

	return &Trigger{
		option:    curOption,
		user:      user,
		messageID: messageID,
	}
}

type TriggerWebhookResult struct {
	WebhookKeyName string
	Url            string
	IsSuccess      bool
	ErrMsg         string
}

func (r TriggerWebhookResult) GetIsSuccess() bool {
	return r.IsSuccess
}

func (r TriggerWebhookResult) GetIsFailed() bool {
	return !r.GetIsSuccess()
}

func newSuccessTriggerWebhookResult(webhookKeyName, url string) TriggerWebhookResult {
	return TriggerWebhookResult{webhookKeyName, url, true, ""}
}

func newFailedTriggerWebhookResult(webhookKeyName, url string, err error) TriggerWebhookResult {
	return TriggerWebhookResult{webhookKeyName, url, false, err.Error()}
}

type TriggerWebhookResultList []TriggerWebhookResult

func (t *Trigger) TriggerWebhook(ctx context.Context) TriggerWebhookResultList {
	ruleWebhookList := make([]*Webhook, 0)
	webhookContent := "this is content struct"

	ctx, cancel := context.WithTimeout(ctx, 10*time.Second)
	defer cancel()

	group, _ := errgroup.WithContext(ctx)
	group.SetLimit(3)
	m := make(map[string]struct{})
	totalTry := 0
	twrc := make(chan TriggerWebhookResult, len(ruleWebhookList))

	for _, w := range ruleWebhookList {
		if !w.Trigger.CreateChatGroup && t.option.FromCreateChatGroup {
			continue
		}

		url := w.Url
		keyName := w.KeyName
		if _, ok := m[keyName]; ok {
			continue
		}
		m[keyName] = struct{}{}
		totalTry++

		group.Go(func() error {
			if err := t.SendWebhook(ctx, url, webhookContent, keyName).Err(); err != nil {
				twrc <- newFailedTriggerWebhookResult(keyName, url, err)
				return fmt.Errorf("send webhook(%s) failed, err: %s", keyName, err.Error())
			}

			twrc <- newSuccessTriggerWebhookResult(keyName, url)
			return nil
		})
	}

	results := make([]TriggerWebhookResult, 0, totalTry)
	for i := 0; i < totalTry; i++ {
		results = append(results, <-twrc)
	}
	if err := group.Wait(); err != nil {
		log.Printf("group wait error: %v", err)
	}

	return results
}

// SendWebhook send webContent to url. KeyName should be keyName of url,
// if there is no keyName for given url then use url as keyName instead.
func (t *Trigger) SendWebhook(ctx context.Context, url string, webContent interface{}, keyName /* keyName or url */ string) SendResult {
	res := &BaseSendResult{}
	defer func(startTime time.Time) {
		t.SendWebhookEnd(ctx, url, webContent, keyName, startTime, res)
	}(time.Now())

	err := SendJsonPost(ctx, url, webContent, nil)
	errMsg := ""
	if err != nil {
		errMsg = err.Error()
	}
	res.Success = err == nil
	res.ErrMsg = errMsg
	res.Error = err

	return res
}
