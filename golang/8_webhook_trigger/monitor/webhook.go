package monitor

import (
	"context"
	"log"
	"sync"
)

const TotalWebhookPrefix = "total_webhook"

// T is a tag struct.
type T struct {
	Name  string
	Value string
}

type MetricsClient struct {
}

func (m *MetricsClient) EmitTimerMetricsEmitTimer(name string, value interface{}, tags ...T) error {
	return nil
}

func NewDefaultMetricsClient(prefix string) *MetricsClient {
	return &MetricsClient{}
}

// 如果这里最后成为性能瓶颈的话，可以考虑 hash & mod -> 找特定的 []map
var metricsClientMapMu sync.Mutex
var metricsClientMap = make(map[string]*MetricsClient)

func getMetricsClient(prefix /* key_name or oldUrl*/ string) *MetricsClient {
	metricsClientMapMu.Lock()
	defer metricsClientMapMu.Unlock()

	c, ok := metricsClientMap[prefix]
	if !ok {
		c = NewDefaultMetricsClient(prefix)
		metricsClientMap[prefix] = c
	}

	return c
}

func EmitWebhookTimer(ctx context.Context, prefix, methodName string, value interface{}, tagskv ...T) {
	err := getMetricsClient(prefix).EmitTimerMetricsEmitTimer(methodName, value, tagskv...)
	if err != nil {
		log.Printf("[Metrics] prefix: %s, methodName: %s, EmitTimer failed, %v ", prefix, methodName, err)
	}

	return
}

func AddResultSuccess() T {
	return addResult("success")
}
func AddResultFailed() T {
	return addResult("failed")
}

func addResult(res string) T {
	return T{Name: "result", Value: res}
}

func AddWebhookTypeNew() T {
	return addWebhookTypeTag("new_webhook")
}

func AddWebhookTypeOld() T {
	return addWebhookTypeTag("old_webhook")
}

func addWebhookTypeTag(webhookType string) T {
	return T{Name: "webhook_type", Value: webhookType}
}

func AddKeyName(keyName string) T {
	return T{Name: "key_name", Value: keyName}
}

func AddTriggerOnTag(triggerOn string) T {
	return T{Name: "trigger_on", Value: triggerOn}
}
