package org.example.webhooktrigger.monitor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

public class Monitor {
    private static final String TOTAL_WEBHOOK_PREFIX = "total_webhook";
    private static final Logger LOGGER = Logger.getLogger(Monitor.class.getName());
    private static final ConcurrentMap<String, MetricsClient> metricsClientMap = new ConcurrentHashMap<>();

    public static void emitWebhookTimer(String prefix, String methodName, long value, Tag... tags) {
        try {
            getMetricsClient(prefix).emitTimerMetrics(methodName, value, tags);
        } catch (Exception e) {
            LOGGER.warning(String.format("[Metrics] prefix: %s, methodName: %s, EmitTimer failed, %s", prefix, methodName, e.getMessage()));
        }
    }

    private static MetricsClient getMetricsClient(String prefix) {
        return metricsClientMap.computeIfAbsent(prefix, MetricsClient::new);
    }

    public static Tag addResultSuccess() {
        return new Tag("result", "success");
    }

    public static Tag addResultFailed() {
        return new Tag("result", "failed");
    }

    public static Tag addWebhookTypeNew() {
        return new Tag("webhook_type", "new_webhook");
    }

    public static Tag addWebhookTypeOld() {
        return new Tag("webhook_type", "old_webhook");
    }

    public static Tag addKeyName(String keyName) {
        return new Tag("key_name", keyName);
    }

    public static Tag addTriggerOnTag(String triggerOn) {
        return new Tag("trigger_on", triggerOn);
    }
}

class MetricsClient {
    private final String prefix;

    public MetricsClient(String prefix) {
        this.prefix = prefix;
    }

    public void emitTimerMetrics(String name, long value, Tag... tags) {
        // Placeholder for actual metric emitting logic
    }
}

