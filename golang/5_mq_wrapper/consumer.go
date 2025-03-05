package __mq_wrapper

import (
	"log"

	"github.com/IBM/sarama"
)

// Consumer handles consuming of messages from a message queue in an async manner.
// Usage:
//
//	consumer := NewConsumer("my-topic", "my-consumer-group",
//		WithErrors(true))
//
//	go func() {
//		for {
//			select {
//			case msg := <-consumer.Messages():
//				// do some processing ...
//				consumer.Commit(msg)
//			case <-consumer.Errors():
//
//			}
//		}
//	}()
type Consumer struct {
	options commonOptions

	topic         string
	consumerGroup string
	consumer      sarama.Consumer
	connFn        func() (sarama.Consumer, error)

	messages chan *Message
	errors   chan error
	close    chan any
	closed   chan any
}

// NewConsumer instantiates a Consumer based on the cluster name, topic and consumerGroup.
func NewConsumer(topic, consumerGroup string, connMethod ConsumerConnectionMethod, opts ...Option) (*Consumer, error) {
	kafkaConfig := *defaultKafkaConfig

	c := &Consumer{
		options: commonOptions{
			kafkaConfig: &kafkaConfig,
		},
		topic:         topic,
		consumerGroup: consumerGroup,
		messages:      make(chan *Message),
		errors:        make(chan error),
		close:         make(chan any),
		closed:        make(chan any),
	}

	for _, opt := range opts {
		opt(c.options)
	}

	connFn := func() (sarama.Consumer, error) {
		return connMethod(c)
	}
	c.connFn = connFn

	consumer, err := c.connFn()
	if err != nil {
		log.Printf("unable to connect to consumer: %v", err)
		return nil, err
	}
	c.consumer = consumer

	go c.run()

	return c, nil
}

// Messages returns the channel of messages that have been consumed.
func (c *Consumer) Messages() <-chan *Message {
	return c.messages
}

// Commit commits the message as being read by the Consumer.
func (c *Consumer) Commit(msg *Message) {
	// Sarama automatically commits offsets, manual commits can be handled if needed

}

// Errors returns the error channel for consume errors.
func (c *Consumer) Errors() <-chan error {
	return c.errors
}

// Close cleans up the Consumer.
func (c *Consumer) Close() error {
	close(c.close)
	defer func() {
		<-c.closed
		close(c.messages)
		close(c.errors)
	}()

	return c.consumer.Close()
}

func (c *Consumer) run() {
	//for {
	//	select {
	//	case <-c.close:
	//		close(c.closed)
	//		return
	//	case msg, ok := <-c.consumer.Messages():
	//		if !ok {
	//			continue
	//		}
	//		if msg == nil {
	//			log.Printf("messages are malformed")
	//			continue
	//		}
	//		c.messages <- &Message{
	//			Key:   string(msg.Key),
	//			Value: string(msg.Value),
	//
	//			rawMsg: msg,
	//		}
	//	case err, ok := <-c.consumer.Errors():
	//		if !ok || err == nil {
	//			continue
	//		}
	//		if errors.Is(err, sarama.ErrClosedClient) {
	//			log.Printf("kafka leader changed to another dc, reconnecting")
	//			c.consumer.Close()
	//			var cErr error
	//			c.consumer, cErr = c.connFn()
	//			if cErr != nil {
	//				log.Printf("failed to reconnect to new dc err:", cErr.Error())
	//			} else {
	//				log.Printf("kafka leader reconnected.")
	//			}
	//		}
	//		c.errors <- err
	//	case ntf, ok := <-c.consumer.Notifications():
	//		if !ok {
	//			continue
	//		}
	//		log.Printf("received kafka consumer notification. Claimed: %v, Released: %v, Current: %v")
	//	}
	//}
}
