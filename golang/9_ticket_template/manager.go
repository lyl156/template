package __ticket_template

import (
	"context"
	"log"
)

type System interface {
	Name() string
	CreateTicket(ctx context.Context, ticketCfg *Ticket) (*TicketResponse, error)
	GetTicket(ctx context.Context, messageID string, ticketSystemName string) (*TicketResponse, error)
}

type SystemManager struct {
	ticketSystems map[string]System
}

func (s *SystemManager) register(name string, sys System) {
	s.ticketSystems[name] = sys
}

var systemMgr = NewSystemManager(
	NewDefaultSystem(),
	// no need to add other systems. Only need to add config in DB.
	// SystemManager should query DB by given ticket name, and get specific ticket config(including endpoint, auth method...)
)

func NewSystemManager(systems ...System) *SystemManager {
	s := &SystemManager{ticketSystems: make(map[string]System)}
	for _, sys := range systems {
		s.register(sys.Name(), sys)
	}

	return s
}

func GetSystemManager() *SystemManager {
	return systemMgr
}

// CreateTicketWithConfig choose system based on given name
func (s *SystemManager) CreateTicketWithConfig(ctx context.Context, name string, ticketCfg *Ticket) (*TicketResponse, error) {
	sys, ok := s.ticketSystems[name]
	if !ok {
		log.Printf("no this ticket system: %s, use default system", name)
		sys = s.ticketSystems["default"]
	}

	log.Printf("[SystemManager][CreateTicketWithConfig]name: %s, ticket config: %+v", name, ticketCfg)
	return sys.CreateTicket(ctx, ticketCfg)
}
