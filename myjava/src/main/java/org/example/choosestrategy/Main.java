package org.example.choosestrategy;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        NotifyServiceImpl notifyService = new NotifyServiceImpl();
        notifyService.notify(NotificationType.ONCE, LocalDateTime.now());
    }
}
