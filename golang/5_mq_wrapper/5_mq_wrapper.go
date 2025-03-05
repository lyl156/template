package __mq_wrapper

import (
	"context"
	"log"
	"os"

	"github.com/IBM/sarama"
)

var (
	defaultKafkaConfig *sarama.Config
)

// IProducer is the implementation of a BMQ producer.
type IProducer interface {
	Produce(ctx context.Context, msg Message) error
	Successes() <-chan Metadata
	Errors() <-chan error
	Close() error
}

// IConsumer is the implementation of a BMQ consumer.
type IConsumer interface {
	Messages() <-chan *Message
	Commit(msg *Message)
	Errors() <-chan error
	Close() error
}

func init() {
	defaultKafkaConfig = sarama.NewConfig()
	defaultKafkaConfig.Producer.Return.Successes = true
	defaultKafkaConfig.Consumer.Offsets.Initial = sarama.OffsetOldest

	if os.Getenv("DEPLOY_ENV") != "debug" {
		// Initialize Sarama logger if not in debug mode.
		sarama.Logger = log.New(os.Stdout, "[Sarama] ", log.LstdFlags)
	}
}

// Message contains the message to be produced or consumed from the message queue.
// Key represents the partition key, it can be omitted.
// Value represents the payload.
type Message struct {
	Key   string
	Value string

	rawMsg *sarama.ConsumerMessage
}

// Metadata contains the metadata regarding the message queue.
type Metadata struct {
	Offset    int64
	Partition int32
}
