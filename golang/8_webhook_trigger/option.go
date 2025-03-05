package __webhook_trigger

import "time"

type Option struct {
	FromAck              bool
	FromCreateChatGroup  bool
	RecordActionTypeFrom string
	FromDeprecatedSource bool
	RecoverTime          *time.Time
}

type Options func(o *Option)

func FromAckOption() Options {
	return func(o *Option) {
		o.FromAck = true
	}
}

func FromRecoveryCard(recoverTime time.Time) Options {
	return func(o *Option) {
		o.RecoverTime = &recoverTime
	}
}

func FromCreateChatGroupOption() Options {
	return func(o *Option) {
		o.FromCreateChatGroup = true
	}
}

func RecordActionTypeFromOption(s string) Options {
	return func(o *Option) {
		o.RecordActionTypeFrom = s
	}
}

func FromDeprecatedSourceOption() Options {
	return func(o *Option) {
		o.FromDeprecatedSource = true
	}
}
