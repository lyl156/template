package org.example.tickettemplate;

public class TicketResponse {
    private String messageId;
    private String creator;
    private String ticketSystemName;
    private String displayTicketLink;
    private String createTicketLink;
    private Ticket config;
    private OptionFromThirdParty optionFromThirdParty;

    public TicketResponse() {}

    public TicketResponse(String messageID, String user, String name, String ticketLink, String s, Ticket cfg, OptionFromThirdParty optionFromThirdParty) {
            this.messageId = messageID;
            this.creator = user;
            this.ticketSystemName = name;
            this.displayTicketLink = ticketLink;
            this.createTicketLink = s;
            this.config = cfg;
            this.optionFromThirdParty = optionFromThirdParty;
    }

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public String getCreator() { return creator; }
    public void setCreator(String creator) { this.creator = creator; }

    public String getTicketSystemName() { return ticketSystemName; }
    public void setTicketSystemName(String ticketSystemName) { this.ticketSystemName = ticketSystemName; }

    public String getDisplayTicketLink() { return displayTicketLink; }
    public void setDisplayTicketLink(String displayTicketLink) { this.displayTicketLink = displayTicketLink; }

    public String getCreateTicketLink() { return createTicketLink; }
    public void setCreateTicketLink(String createTicketLink) { this.createTicketLink = createTicketLink; }

    public Ticket getConfig() { return config; }
    public void setConfig(Ticket config) { this.config = config; }

    public OptionFromThirdParty getOptionFromThirdParty() { return optionFromThirdParty; }
    public void setOptionFromThirdParty(OptionFromThirdParty optionFromThirdParty) { this.optionFromThirdParty = optionFromThirdParty; }
}

class OptionFromThirdParty {
    private String chatGroupId;
    private boolean sendCardToChatGroup;

    public OptionFromThirdParty(String chatGroupId, boolean sendCardToChatGroup) {
        this.chatGroupId = chatGroupId;
        this.sendCardToChatGroup = sendCardToChatGroup;
    }

    public String getChatGroupId() { return chatGroupId; }
    public void setChatGroupId(String chatGroupId) { this.chatGroupId = chatGroupId; }

    public boolean isSendCardToChatGroup() { return sendCardToChatGroup; }
    public void setSendCardToChatGroup(boolean sendCardToChatGroup) { this.sendCardToChatGroup = sendCardToChatGroup; }
}

class CreateTicketResp {
    private String ticketLink;
    private String chatGroupId;
    private boolean sendCardToChatGroup;

    public String getTicketLink() { return ticketLink; }
    public void setTicketLink(String ticketLink) { this.ticketLink = ticketLink; }

    public String getChatGroupId() { return chatGroupId; }
    public void setChatGroupId(String chatGroupId) { this.chatGroupId = chatGroupId; }

    public boolean isSendCardToChatGroup() { return sendCardToChatGroup; }
    public void setSendCardToChatGroup(boolean sendCardToChatGroup) { this.sendCardToChatGroup = sendCardToChatGroup; }
}

class Content {

}