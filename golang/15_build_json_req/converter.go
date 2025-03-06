package _5_build_json_req

import (
	"encoding/json"
	"log"
	"unsafe"
)

func ToJsonStr(data interface{}) string {
	s, err := json.Marshal(data)
	if err != nil {
		log.Printf("json marshal err: %s, data: %+v", err.Error(), data)
		return ""
	}

	return BytesToString(s)
}

// BytesToString convert []byte type to string type.
// https://pkg.go.dev/github.com/henrylee2cn/goutil
func BytesToString(b []byte) string {
	return *(*string)(unsafe.Pointer(&b))
}
