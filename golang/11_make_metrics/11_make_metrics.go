package _1_make_metrics

import (
	"net/http"

	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promhttp"
)

var (
	// 定义 Prometheus CounterVec 指标
	xxCount = prometheus.NewCounterVec(
		prometheus.CounterOpts{
			Name: "xx_total",
			Help: "Count of xx usage in yy process",
		},
		[]string{"status"}, // 维度标签
	)
)

func Init() {
	// 注册指标
	prometheus.MustRegister(xxCount)
}

// IncrementMetric 增加指定状态的计数
func IncrementMetric(status string) {
	xxCount.WithLabelValues(status).Inc()
}

func main() {
	Init()

	// 模拟打点
	IncrementMetric("failure")

	// 启动 HTTP 服务器，暴露 /metrics 端点
	http.Handle("/metrics", promhttp.Handler())
	http.ListenAndServe(":8080", nil)
}
