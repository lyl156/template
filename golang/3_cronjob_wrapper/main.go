package __cronjob_wrapper

import "fmt"

func main() {
	Init()
	defer Close()

	// init real service dependent on cronjob
	cs := NewService(SchedulerFunc(AddCronJob))
	fmt.Println(cs)
}
