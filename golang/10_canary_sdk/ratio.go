package _0_canary_sdk

import (
	"hash/fnv"
	"log"
)

const (
	// 放量范围0-100
	MinRatio = 0
	MaxRatio = 100
)

func CompareRatio(input string, target int) bool {
	if target <= MinRatio { // 如果配置的放量小于等于0，就直接返回不在放量内
		return false
	}
	if target >= MaxRatio { // 如果大于等于100，直接默认在放量内
		return true
	}
	ratio := getRatioValue(input)
	result := ratio <= uint32(target)
	log.Printf("result = %t, hash = %d, target = %d, input = %s", result, ratio, target, input)

	return result
}

func getRatioValue(input string) uint32 {
	h := fnv.New32a()             // hash算法
	_, _ = h.Write([]byte(input)) // 输入参数

	return h.Sum32() % MaxRatio // 计算hash并取余，期望得到一个0-99的数字
}
