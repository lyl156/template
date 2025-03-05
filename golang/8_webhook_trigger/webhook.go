package __webhook_trigger

import "time"

type TriggerData struct {
	SendAlert       bool `json:"send_alert" bson:"send_alert"`               // trigger webhook on sending alert notification
	Ack             bool `json:"ack" bson:"ack"`                             // trigger webhook on user acknowledgement the alert notification
	CreateChatGroup bool `json:"create_chat_group" bson:"create_chat_group"` // trigger webhook on user create the chat group
}

type Webhook struct {
	Id            string          `json:"id"`              // unique id in database for webhook
	KeyName       string          `json:"key_name"`        // unique name for webhook
	DisplayName   string          `json:"display_name"`    // name of webhook displayed on Web UI
	CreatedBy     string          `json:"created_by"`      // creator of this webhook.
	LastUpdatedBy string          `json:"last_updated_by"` // operator which update this webhook last time.
	Admins        []string        `json:"admins"`          // Administrators of webhook.
	Type          string          `json:"type"`            // type of webhook
	Url           string          `json:"url"`             // url of webhook, request address
	HttpMethod    string          `json:"http_method"`     // http method of webhook
	Desc          Description     `json:"description"`     // multi-lang description of this webhook
	DescOld       string          `json:"desc,omitempty"`  // legacy description of this webhook
	Trigger       TriggerData     `json:"trigger"`         // trigger webhook at what time
	Status        string          `json:"status"`          // status of webhook, like "deleted" / "normal"
	UpdatedAt     time.Time       `json:"updated_at"`      // this webhook was updated at what time
	ExpireTime    time.Time       `json:"expire_time"`     // this webhook was expired at what time
	Params        []*WebhookParam `json:"params"`          // custom params config of webhook
}

type Description struct {
	Chinese string `json:"chinese" valid:"runeCount('$', 500)"` // chinese description of this entity
	English string `json:"english" valid:"runeCount('$', 500)"` // english description of this entity
}

type WebhookParams []*WebhookParam

type WebhookParam struct {
	Type         string      `json:"type" valid:"in($, 'var', 'fixed')"`                         // type of webhook params like: "var/fixed", represents whether param value is a fixed value or get from vars
	Key          string      `json:"key" valid:"len($) <= 1024"`                                 // key of param which customized by user
	Value        string      `json:"-" valid:"len($) <= 4096"`                                   // value of param, a fixed value or vars from alert runtime context
	DefaultValue string      `json:"default_value" valid:"runeCount('$', 500)"`                  // fill this default to param value when get vars from alert context failed
	ValueType    string      `json:"value_type" valid:"in($, 'float', 'string', 'bool', 'int')"` // data type of value, float/int/string/bool
	Desc         Description `json:"description"`                                                // description of this param
}
