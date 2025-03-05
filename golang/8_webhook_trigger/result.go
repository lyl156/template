package __webhook_trigger

import "errors"

type SendResult interface {
	IsSuccess() bool
	GetMsgId() string
	Err() error
}

type BaseSendResult struct {
	Success bool   `json:"is_success" bson:"is_success"`
	MsgId   string `json:"msg_id,omitempty" bson:"msg_id,omitempty"`
	ErrMsg  string `json:"err_msg" bson:"err_msg"`
	Error   error  `json:"-" bson:"-"`
}

func (b *BaseSendResult) IsSuccess() bool {
	return b.Success
}

func (b *BaseSendResult) GetMsgId() string {
	return b.MsgId
}

func (b *BaseSendResult) Err() error {
	if b.Error != nil {
		return b.Error
	}

	if b.ErrMsg != "" {
		b.Error = errors.New(b.ErrMsg)
	}

	return b.Error
}
