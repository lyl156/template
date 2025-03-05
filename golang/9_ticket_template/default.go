package __ticket_template

import (
	"context"
	"log"
)

type defaultSystem struct {
	name                   string
	createTicketLockPrefix string
	postFunc               func(ctx context.Context, req *Content, user string) (*CreateTicketResp, error)
	fillReqFunc            func(req *Content) error
}

func NewDefaultSystem() System {
	d := &defaultSystem{
		createTicketLockPrefix: "default_create_ticket_",
		postFunc:               createDefaultTicket,
	}
	d.setSystemName("default")

	return d
}

func (d *defaultSystem) Name() string {
	return d.name
}

func (d *defaultSystem) CreateTicket(ctx context.Context, ticketCfg *Ticket) (*TicketResponse, error) {
	// 1. build content

	// 2. send request and get response
	bizEntity, err := d.postRequest(ctx, nil /* content */, ticketCfg, "userID", "messageID", "ticketSystem name")
	if err != nil {
		return nil, err
	}

	// 3. save response into mongo
	go d.saveResponse(ctx, bizEntity, "messageID")

	// return response
	return bizEntity, nil
}

func (d *defaultSystem) GetTicket(ctx context.Context, messageID string, ticketSystemName string) (*TicketResponse, error) {
	// query from db to get created ticket

	// convert db object to ticketResponse
	return &TicketResponse{}, nil
}

func (d *defaultSystem) postRequest(ctx context.Context, req *Content, cfg *Ticket, user, messageID string,
	name string) (*TicketResponse, error) {
	// 1. get distributed lock by messageID

	// 2. send http req to third party system
	res, err := d.postFunc(ctx, req, user)
	if err != nil {
		log.Printf("[PostRequest] system name: %s, postFunc error: %s, req: %v, user: %s", name, err.Error(), &req, user)
		return nil, err
	}

	return &TicketResponse{
		MessageId:         messageID,
		Creator:           user,
		TicketSystemName:  name,
		Config:            cfg,
		DisplayTicketLink: res.TicketLink,
		OptionFromThirdParty: OptionFromThirdParty{
			ChatGroupId:         res.ChatGroupId,
			SendCardToChatGroup: res.SendCardToChatGroup,
		},
	}, nil
}

func (d *defaultSystem) setSystemName(systemName string) {
	d.name = systemName
}

// createDefaultTicket is defaultSystem's postFunc
func createDefaultTicket(ctx context.Context, req *Content, _ string) (*CreateTicketResp, error) {
	// get endpoint from Ticket.SiteKeyAndEndpoints based on ticket name
	// call third party ticket api
	return &CreateTicketResp{}, nil
}

func (d *defaultSystem) saveResponse(ctx context.Context, result *TicketResponse, messageID string) {
	// 1. store result to DB
}
