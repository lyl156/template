package org.example.choosestrategy;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Logger;

enum NotificationType {
    ONCE, INTERVAL
}

@FunctionalInterface
interface Notifier {
    void notify(LocalDateTime curTime);
}

class NotifyServiceImpl {
    private static final Logger logger = Logger.getLogger(NotifyServiceImpl.class.getName());
    private final Map<NotificationType, Notifier> notifiers = new EnumMap<>(NotificationType.class);

    public NotifyServiceImpl() {
        registerNotifiers();
    }

    private void registerNotifiers() {
        notifiers.put(NotificationType.INTERVAL, this.intervalNotifier());
        notifiers.put(NotificationType.ONCE, this.onceNotifier());
    }

    private Notifier intervalNotifier() {
        return curTime -> logger.info("I am intervalNotifier, time: " + curTime);
    }

    private Notifier onceNotifier() {
        return curTime -> logger.info("I am onceNotifier, time: " + curTime);
    }

    public void notify(NotificationType type, LocalDateTime curTime) {
        Notifier notifier = notifiers.get(type);
        if (notifier == null) {
            throw new IllegalArgumentException("Unsupported notifierType: " + type);
        }
        notifier.notify(curTime);
    }
}
