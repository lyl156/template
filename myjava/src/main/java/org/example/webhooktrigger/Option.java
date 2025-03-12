package org.example.webhooktrigger;

import java.time.LocalDateTime;

public class Option {
    public boolean isFromAck() {
        return fromAck;
    }

    public boolean isFromCreateChatGroup() {
        return fromCreateChatGroup;
    }

    public String getRecordActionTypeFrom() {
        return recordActionTypeFrom;
    }

    public boolean isFromDeprecatedSource() {
        return fromDeprecatedSource;
    }

    public LocalDateTime getRecoverTime() {
        return recoverTime;
    }

    private boolean fromAck;
    private boolean fromCreateChatGroup;
    private String recordActionTypeFrom;
    private boolean fromDeprecatedSource;
    private LocalDateTime recoverTime;

    // **私有构造方法，防止直接使用 new Option()**
    private Option(Builder builder) {
        this.fromAck = builder.fromAck;
        this.fromCreateChatGroup = builder.fromCreateChatGroup;
        this.recordActionTypeFrom = builder.recordActionTypeFrom;
        this.fromDeprecatedSource = builder.fromDeprecatedSource;
        this.recoverTime = builder.recoverTime;
    }

    public static class Builder {
        private boolean fromAck;
        private boolean fromCreateChatGroup;
        private String recordActionTypeFrom;
        private boolean fromDeprecatedSource;
        private LocalDateTime recoverTime;

        public Builder fromAck(boolean fromAck) {
            this.fromAck = fromAck;
            return this;
        }

        public Builder fromCreateChatGroup(boolean fromCreateChatGroup) {
            this.fromCreateChatGroup = fromCreateChatGroup;
            return this;
        }

        public Builder recordActionTypeFrom(String recordActionTypeFrom) {
            this.recordActionTypeFrom = recordActionTypeFrom;
            return this;
        }

        public Builder fromDeprecatedSource(boolean fromDeprecatedSource) {
            this.fromDeprecatedSource = fromDeprecatedSource;
            return this;
        }

        public Builder recoverTime(LocalDateTime recoverTime) {
            this.recoverTime = recoverTime;
            return this;
        }

        public Option build() {
            return new Option(this);
        }
    }}
