package org.example.webhooktrigger;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Option option = new Option.Builder()
                .fromAck(true)
                .fromCreateChatGroup(false)
                .recordActionTypeFrom("user_action")
                .fromDeprecatedSource(false)
                .recoverTime(LocalDateTime.now())
                .build();

        Trigger trigger = new Trigger("user123", "msg456", option);
        trigger.triggerWebhook();
        System.out.println("Trigger created successfully");
    }
}
