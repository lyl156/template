package __mq_wrapper

import (
	"context"
	"fmt"
	"log"

	"github.com/IBM/sarama"
)

// Producer handles the producing of messages to a message queue in an async manner.
// Usage:
//
//	producer := NewProducer("my-topic",
//		WithSuccesses(True),
//		WithErrors(True))
//
//	go func () {
//		for {
//			select {
//				case <-producer.Successes():
//
//				case <-producer.Errors():
//
//			}
//		}
//	}()
//
//	producer.Produce(context.Background(), Message{Key: "key", Value: "value"})
type Producer struct {
	options commonOptions

	topic    string
	producer sarama.AsyncProducer

	successes chan Metadata
	errors    chan error
	closing   chan any
	closed    chan any
}

// NewProducer instantiates a Producer based on the cluster name and topic.
func NewProducer(topic string, connMethod ProducerConnectionMethod, opts ...Option) *Producer {
	kafkaConfig := *defaultKafkaConfig
	p := &Producer{
		options: commonOptions{
			kafkaConfig: &kafkaConfig,
		},
		topic:     topic,
		successes: make(chan Metadata),
		errors:    make(chan error),
		closing:   make(chan any),
		closed:    make(chan any),
	}

	for _, opt := range opts {
		opt(p.options)
	}

	asyncProducer, err := connMethod(p.options)
	if err != nil {
		log.Printf("unable to start producer err: %s", err.Error())
		return nil
	}
	p.producer = asyncProducer

	go p.run()

	return p
}

// Produce produces the Message in an async manner.
func (p *Producer) Produce(ctx context.Context, message Message) error {
	pMsg := &sarama.ProducerMessage{
		Topic: p.topic,
		Value: sarama.StringEncoder(message.Value),
	}

	if message.Key != "" {
		pMsg.Key = sarama.StringEncoder(message.Key)
	}

	return nil
	//return p.producer.CtxSendMessage(ctx, pMsg)
}

// Successes returns a channel with the success.
func (p *Producer) Successes() <-chan Metadata {
	return p.successes
}

// Errors returns a channel for error.
func (p *Producer) Errors() <-chan error {
	return p.errors
}

// Close cleans up Producer cleanly.
func (p *Producer) Close() error {
	close(p.closing)
	defer func() {
		<-p.closed
		close(p.successes)
		close(p.errors)
	}()
	return p.producer.Close()
}

func (p *Producer) run() {
	for {
		select {
		case <-p.closing:
			close(p.closed)
			return
		case success := <-p.producer.Successes():
			if success == nil {
				continue
			}
			p.successes <- Metadata{
				Offset:    success.Offset,
				Partition: success.Partition,
			}
		case err := <-p.producer.Errors():
			if err == nil {
				continue
			}
			var (
				key, value string
				offset     int64
				partition  int32
			)
			if err.Msg != nil {
				if err.Msg.Key != nil {
					se, _ := err.Msg.Key.(sarama.StringEncoder)
					key = string(se)
				}
				if err.Msg.Value != nil {
					se, _ := err.Msg.Value.(sarama.StringEncoder)
					value = string(se)
				}
				offset = err.Msg.Offset
				partition = err.Msg.Partition
			}

			p.errors <- MessageError{
				topic:     p.topic,
				key:       key,
				value:     value,
				offset:    offset,
				partition: partition,
				err:       err.Err,
			}
		}
	}
}

func (p *Producer) HandleResult() {
	for {
		select {
		case <-p.closing:
			return
		case success := <-p.Successes():
			log.Printf(fmt.Sprintf("produce successfully, partition: %d offset: %d", success.Partition, success.Offset))
		case err := <-p.Errors():
			log.Printf(fmt.Sprintf("produce failed, err: %s", err.Error()))
		}
	}
}
