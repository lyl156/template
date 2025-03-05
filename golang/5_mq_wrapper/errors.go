package __mq_wrapper

import "fmt"

// MessageError represents the errror on handling messages either on the Producer or Consumer side.
type MessageError struct {
	topic     string
	key       string
	value     string
	offset    int64
	partition int32

	err error
}

// Error implements the error interface.
func (me MessageError) Error() string {
	return fmt.Sprintf("topic: %s offset: %d partition: %d key: %s value: %s err: %v",
		me.topic, me.offset, me.partition, me.key, me.value, me.err)
}

// Unwrap returns the underlying error.
func (me MessageError) Unwrap() error {
	return me.err
}
