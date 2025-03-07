package _6_limiter_factory

import "fmt"

func main() {
	MustInit()
	l, err := GlobalFactory.GenLimiter(true)
	if err != nil {
		fmt.Println(err)
	} else {
		fmt.Println(l)
	}
}
