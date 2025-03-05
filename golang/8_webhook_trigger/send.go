package __webhook_trigger

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"strings"
	"time"

	"github.com/moul/http2curl"

	"github.com/lyl156/template/8_webhook_trigger/monitor"
)

type TagKv map[string]string

func escapeHTTP(url string) string {
	return strings.Replace(url, " ", `%20`, -1)
}

var httpCli = &http.Client{
	Timeout: 10 * time.Second,
	Transport: &http.Transport{
		MaxIdleConns:      10,               // 最大空闲连接数
		IdleConnTimeout:   30 * time.Second, // 空闲连接超时
		DisableKeepAlives: false,            // 启用 KeepAlive
	},
}

func HttpJSONPostNoResp(ctx context.Context, url string, reqModel interface{}, header map[string]string) (int, error) {
	url = escapeHTTP(url)
	var jsonData []byte
	if reqStr, ok := reqModel.(string); ok {
		jsonData = []byte(reqStr)
	} else {
		var err error
		jsonData, err = json.Marshal(reqModel)
		if err != nil {
			return 0, fmt.Errorf("failed to marshal request: %w", err)
		}
	}

	req, err := http.NewRequestWithContext(ctx, "POST", url, bytes.NewReader(jsonData))
	if err != nil {
		return 0, fmt.Errorf("failed to create request: %w", err)
	}

	req.Header.Set("Content-Type", "application/json")
	for k, v := range header {
		req.Header.Set(k, v)
	}

	command, err := http2curl.GetCurlCommand(req)
	if err != nil {
		log.Printf("error when GetCurlCommand: %v", err)
	}

	resp, err := httpCli.Do(req)
	if err != nil {
		return 0, fmt.Errorf("failed to send request: %w", err)
	}
	defer resp.Body.Close()

	if command != nil {
		log.Printf("[HttpClient Request] curl = [%s], response status = [%d]", command.String(), resp.StatusCode)
	}

	return resp.StatusCode, nil
}

func SendJsonPost(ctx context.Context, url string, reqModel interface{}, header map[string]string) error {
	// TODO: make metrics
	tagkv := TagKv{
		"status": "success",
	}

	logErrMsg := "send failed"
	if logID, ok := ctx.Value("log_id").(string); ok {
		logErrMsg = fmt.Sprintf("log_id: %s, send failed", logID)
	}

	statusCode, err := HttpJSONPostNoResp(ctx, url, reqModel, header)
	if err != nil {
		tagkv["status"] = "error"
		return fmt.Errorf("%s: %w", logErrMsg, err)
	}

	if statusCode != http.StatusOK {
		tagkv["status"] = "error"
		return fmt.Errorf("%s: unexpected status code: %d", logErrMsg, statusCode)
	}
	return nil
}

// SendWebhookEnd make log and metrics
func (t *Trigger) SendWebhookEnd(ctx context.Context, url string, webContent interface{}, keyName string, startTime time.Time, sr *BaseSendResult) {
	endTime := time.Now()
	executeTime := endTime.Sub(startTime)
	if !sr.IsSuccess() {
		log.Printf("send webhook failed, startTime is %s, endTime is %s, execute time is %s, webHookURL: %s, req: %v, messageID: %s, user: %s, err: %s",
			startTime.String(), endTime.String(), executeTime.String(), url, &webContent, t.messageID, t.user, sr.Err().Error())
	} else {
		log.Printf("send webhook succeed, startTime is %s, endTime is %s, execute time is %s, webHookURL: %s, req: %v, messageID: %s, user: %s",
			startTime.String(), endTime.String(), executeTime.String(), url, &webContent, t.messageID, t.user)
	}

	// metricsName: key_name/url + "webhook_cost"
	// metricsVal: executeTime
	// tagKey:
	// 1. 触发type(trigger_on): send/ack/silence...
	// 2. webhook 类型(webhook_type)：new, old
	// 3. 请求是否成功(result)
	triggerOn := t.option.RecordActionTypeFrom
	if len(triggerOn) == 0 {
		triggerOn = "send"
	}
	var resTag monitor.T
	if sr != nil && sr.IsSuccess() {
		resTag = monitor.AddResultSuccess()
	} else {
		resTag = monitor.AddResultFailed()
	}

	var webhookTypeTag monitor.T
	if t.option.FromDeprecatedSource {
		webhookTypeTag = monitor.AddWebhookTypeOld()
	} else {
		webhookTypeTag = monitor.AddWebhookTypeNew()
	}

	// for end user
	monitor.EmitWebhookTimer(ctx, keyName, webhookCost, executeTime.Milliseconds(),
		monitor.AddTriggerOnTag(triggerOn), webhookTypeTag, resTag)

	// for our system
	monitor.EmitWebhookTimer(ctx, monitor.TotalWebhookPrefix, webhookCost, executeTime.Milliseconds(),
		monitor.AddTriggerOnTag(triggerOn), webhookTypeTag, resTag, monitor.AddKeyName(keyName))
}

const (
	webhookCost = "webhook_cost"
)
