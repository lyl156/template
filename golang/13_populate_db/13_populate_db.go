package _3_populate_db

import (
	"context"
	"log"
	"regexp"
)

func populateXX() {
	logId := "logID"
	ctx := context.WithValue(context.Background(), "logID_Key", logId)

	cnt := 0
	for mockData := range ScanApproval(ctx) {
		if mockData == nil {
			continue
		}
		if !isValidMongoID(ctx, mockData.BusinessID) {
			log.Printf("approval is not procurement demand, mockData ID: %d", mockData.ID)
			continue
		}

		if err := fillMockData(ctx, mockData); err != nil {
			log.Printf("mockData ID: %d, businessID: %s, fillApproval err: %s", mockData.ID, mockData.BusinessID, err.Error())
		} else {
			log.Printf("mockData ID: %d, businessID: %s, fillApproval succeed", mockData.ID, mockData.BusinessID)
			cnt++
		}
	}

	log.Printf("populateApprovalName count: %d", cnt)
}

func isValidMongoID(ctx context.Context, id string) bool {
	// 正则表达式，匹配 MongoDB ObjectId
	mongoIDPattern := "^[a-fA-F0-9]{24}$"

	// 使用 regexp.MatchString 方法检查字符串是否匹配正则
	matched, err := regexp.MatchString(mongoIDPattern, id)
	if err != nil {
		log.Printf("Error while matching: %s", err.Error())
		return false
	}

	return matched
}

func fillMockData(ctx context.Context, mockData *MockData) error {
	log.Printf("mockData ID: %d", mockData.ID)

	// get other data corresponding to mockData
	// update other data
	// store other data

	return nil
}
