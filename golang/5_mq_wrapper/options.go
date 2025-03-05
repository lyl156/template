package __mq_wrapper

import "github.com/IBM/sarama"

type commonOptions struct {
	kafkaConfig *sarama.Config
}

// Option allows the configuration of the BMQ behaviour.
type Option func(commonOptions)

// WithErrors sets the flag on whether Producer.Errors() or Consumer.Errors() are pushed with error data.
// If this is set as true, Producer.Errors() or Consumer.Errors must be called and the underlying channel must be consumed.
func WithErrors(want bool) Option {
	return func(options commonOptions) {
		options.kafkaConfig.Producer.Return.Errors = want
		options.kafkaConfig.Consumer.Return.Errors = want
	}
}

// WithSuccesses sets the flag on whether Producer.Successes() is pushed with the success data.
// If this is set as true, Producer.Successes() must be called and the underlying channel must be consumed.
func WithSuccesses(want bool) Option {
	return func(options commonOptions) {
		options.kafkaConfig.Producer.Return.Successes = want
	}
}

// ProducerConnectionMethod represents the available connection methods for the producer.
type ProducerConnectionMethod func(commonOptions) (sarama.AsyncProducer, error)

// UsingProducerAddressMethod sets the producer connection to be via IP address.
func UsingProducerAddressMethod(addrs []string) ProducerConnectionMethod {
	return func(options commonOptions) (sarama.AsyncProducer, error) {
		return sarama.NewAsyncProducer(addrs, options.kafkaConfig)
	}
}

// ConsumerConnectionMethod represents the available connection methods for the consumer.
type ConsumerConnectionMethod func(c *Consumer) (sarama.Consumer, error)

// UsingConsumerAddressMethod sets the consumer connection to be via IP address.
func UsingConsumerAddressMethod(addrs []string) ConsumerConnectionMethod {
	return func(c *Consumer) (sarama.Consumer, error) {
		return sarama.NewConsumer(addrs, c.options.kafkaConfig)
	}
}
