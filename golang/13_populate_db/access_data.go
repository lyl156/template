package _3_populate_db

import (
	"context"
	"fmt"
	"log"
)

type MockData struct {
	ID         uint64
	BusinessID string
}

type DB struct {
}

func getDB(ctx context.Context) DB {
	return DB{}
}

func ScanApproval(ctx context.Context) <-chan *MockData {
	ch := make(chan *MockData)
	go func() {
		defer func() {
			if r := recover(); r != nil {
				log.Printf("Recovered from panic: %v", r)
			}
		}()

		var offset uint64
		limit := 500
		for {
			db := getDB(ctx)
			var mockDataList []*MockData

			fmt.Println(db)
			fmt.Println(offset)
			//if err := db.WithContext(ctx).Where("id > ?", offset).Order("id asc").Limit(limit).Find(&MockData).Error; err != nil {
			//	break
			//}
			for _, mockData := range mockDataList {
				ch <- mockData
			}

			if len(mockDataList) < 500 {
				break
			}
			offset = mockDataList[limit-1].ID
		}

		close(ch)
	}()

	return ch
}
