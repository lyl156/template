package __ticket_template

type TicketResponse struct {
	MessageId            string
	Creator              string
	TicketSystemName     string
	DisplayTicketLink    string
	CreateTicketLink     string
	Config               *Ticket
	OptionFromThirdParty OptionFromThirdParty
}

type OptionFromThirdParty struct {
	ChatGroupId         string `json:"chat_group_id" bson:"chat_group_id"`
	SendCardToChatGroup bool   `json:"send_card_to_chat_group" bson:"send_card_to_chat_group"`
}

type Content struct {
}

type CreateTicketResp struct {
	TicketLink          string `json:"ticket_link"`
	ChatGroupId         string `json:"chat_group_id"`
	SendCardToChatGroup bool   `json:"send_card_to_chat_group"`
}
