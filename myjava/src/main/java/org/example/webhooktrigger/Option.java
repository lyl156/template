package org.example.webhooktrigger;

import java.time.LocalDateTime;

public class Option {
    private boolean fromAck;
    private boolean fromCreateChatGroup;
    private String recordActionTypeFrom;
    private boolean fromDeprecatedSource;
    private LocalDateTime recoverTime;

    public boolean isFromAck() { return fromAck; }
    public void setFromAck(boolean fromAck) { this.fromAck = fromAck; }

    public boolean isFromCreateChatGroup() { return fromCreateChatGroup; }
    public void setFromCreateChatGroup(boolean fromCreateChatGroup) { this.fromCreateChatGroup = fromCreateChatGroup; }

    public String getRecordActionTypeFrom() { return recordActionTypeFrom; }
    public void setRecordActionTypeFrom(String recordActionTypeFrom) { this.recordActionTypeFrom = recordActionTypeFrom; }

    public boolean isFromDeprecatedSource() { return fromDeprecatedSource; }
    public void setFromDeprecatedSource(boolean fromDeprecatedSource) { this.fromDeprecatedSource = fromDeprecatedSource; }

    public LocalDateTime getRecoverTime() { return recoverTime; }
    public void setRecoverTime(LocalDateTime recoverTime) { this.recoverTime = recoverTime; }
}
