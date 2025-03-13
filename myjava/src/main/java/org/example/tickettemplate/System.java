package org.example.tickettemplate;

public interface System {
    String getName();
    TicketResponse createTicket(String context, Ticket ticketCfg) throws Exception;
    TicketResponse getTicket(String context, String messageID, String ticketSystemName) throws Exception;
}
