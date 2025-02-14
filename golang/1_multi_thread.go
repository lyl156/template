package golang

import (
	"context"
	"fmt"
	"sync"

	"golang.org/x/sync/errgroup"
)

type ItemType struct {
	ID string
}

type BusinessType int

const (
	BusinessType_Example BusinessType = iota
)

type Service struct{}

func (s *Service) BreakdownRepo_FindBreakdownsByAggregateID(ctx context.Context, id string, businessType BusinessType) ([]*Breakdown, error) {
	// 模拟查询
	return []*Breakdown{{ID: "1"}}, nil
}

func (s *Service) AccrualRepo_QueryAccrualsByStatementParams(ctx context.Context, req *RequestByStatement) ([]*OaAccrual, error) {
	// 模拟查询
	return []*OaAccrual{{ID: "1"}}, nil
}

type Breakdown struct {
	ID string
}

func (b *Breakdown) GetID() string {
	return b.ID
}

type RequestByStatement struct {
	BusinessType BusinessType
	PartnerID    string
	Month        string
	OaContractID string
	OurEntity    string
	MediaSource  string
	Region       string
	AppID        string
}

type OaAccrual struct {
	ID string
}

func multiThread() {
	// 创建服务对象
	srv := &Service{}

	// 模拟输入数据
	items := []ItemType{{ID: "item1"}, {ID: "item2"}}
	businessType := BusinessType_Example
	month := "2025-02"

	// 创建 ctx 和 errgroup
	ctx := context.Background()
	g, ctx := errgroup.WithContext(ctx)
	g.SetLimit(10) // 并发限制

	mu := &sync.Mutex{}
	accrualList := make([]*OaAccrual, 0)

	for _, item := range items {
		item := item // 确保每个 goroutine 有独立的 item

		g.Go(func() error {
			switch businessType {
			case BusinessType_Example:
				// 执行查询操作
				brkList, err := srv.BreakdownRepo_FindBreakdownsByAggregateID(ctx, item.ID, businessType)
				if err != nil {
					return fmt.Errorf("error finding breakdowns: %w", err)
				}

				for _, brk := range brkList {
					fmt.Printf("brk: %v", brk)
					req := RequestByStatement{
						BusinessType: businessType,
						PartnerID:    "partnerID", // 示例数据
						Month:        month,
						OaContractID: "oaContractID", // 示例数据
						OurEntity:    "ourEntity",    // 示例数据
						MediaSource:  "mediaSource",  // 示例数据
						Region:       "region",       // 示例数据
						AppID:        "appID",        // 示例数据
					}

					// 执行查询
					curAccrualList, err := srv.AccrualRepo_QueryAccrualsByStatementParams(ctx, &req)
					if err != nil {
						return fmt.Errorf("error querying accruals: %w", err)
					}

					// 结果添加到 accrualList
					if len(curAccrualList) > 0 {
						mu.Lock()
						accrualList = append(accrualList, curAccrualList...)
						mu.Unlock()
					}
				}
			default:
				// 处理其他业务类型
			}
			return nil
		})
	}

	// 等待所有 goroutines 完成
	if err := g.Wait(); err != nil {
		fmt.Println("Error:", err)
		return
	}

	// 打印结果
	fmt.Println("Accrual List:", accrualList)
}
