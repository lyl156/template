package __mq_wrapper

import "fmt"

func main() {
	producerConn := UsingProducerAddressMethod([]string{"127.0.0.1:xxxx"})
	dutyProducer := NewProducer("topic",
		producerConn,
		WithSuccesses(true), WithErrors(true))
	go dutyProducer.HandleResult()

	consumerConn := UsingConsumerAddressMethod([]string{"127.0.0.1:xxxx"})
	dutyConsumer, _ := NewConsumer("topic", "consumerGroup",
		consumerConn,
		WithErrors(true))
	fmt.Println(dutyConsumer)
}
