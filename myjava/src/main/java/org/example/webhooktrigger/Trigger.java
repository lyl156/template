package org.example.webhooktrigger;

import org.example.webhooktrigger.monitor.Monitor;
import org.example.webhooktrigger.monitor.Tag;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.OutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Trigger {
    private Option option;
    private String user;
    private String messageID;

    private static final Logger LOGGER = Logger.getLogger(Trigger.class.getName());
    private static final String WEBHOOK_COST = "webhook_cost";
    private static final String TOTAL_WEBHOOK_PREFIX = "total_webhook";

    public Trigger(String user, String messageID, Option option) {
        this.user = user;
        this.messageID = messageID;
        this.option = option;
    }

    public List<TriggerWebhookResult> triggerWebhook() {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // CompletionService<V> 管理了一组 Future<V>，
        // 并提供了 take() 和 poll() 方法，
        // 让我们按照完成顺序获取 Future 结果，而不是按照任务提交的顺序。
//        Feature	        Future	               CompletionService
        //任务管理方式	    独立管理每个异步任务	    统一管理多个任务
        //任务结果获取顺序	    按照提交顺序获取	        按照完成顺序 获取
        //是否支持并发获取结果	否，需要遍历所有 Future	是，谁先完成先取谁
        //适用场景	        少量异步任务	            批量任务处理、提高吞吐量
        CompletionService<TriggerWebhookResult> completionService = new ExecutorCompletionService<>(executor);
        // CopyOnWriteArrayList 是 线程安全 的 ArrayList 变体，它的关键特性是读操作无锁，写操作复制整个底层数组。适用于读多写少的并发场景。
        List<TriggerWebhookResult> results = new CopyOnWriteArrayList<>();
        List<Webhook> ruleWebhookList = new ArrayList<>(); // Mocked list
        String webhookContent = "this is content struct";

        for (Webhook webhook : ruleWebhookList) {
            completionService.submit(() -> sendWebhook(webhook.getUrl(), webhookContent, webhook.getKeyName()));
        }

        try {
            for (int i = 0; i < ruleWebhookList.size(); i++) {
                // completionService.take() 从已完成的任务队列中取出最早完成的任务（阻塞等待）
                Future<TriggerWebhookResult> future = completionService.take();
                results.add(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        executor.shutdown();

        return results;
    }

    private TriggerWebhookResult sendWebhook(String url, String content, String keyName) {
        Instant startTime = Instant.now();
        TriggerWebhookResult result = new TriggerWebhookResult();
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            // 能被外层 catch 处理
            try (OutputStream os = con.getOutputStream()) {
                os.write(content.getBytes());
                os.flush();
            }

            int responseCode = con.getResponseCode();
            result = responseCode == 200 ? newSuccessTriggerWebhookResult(keyName, url)
                    : newFailedTriggerWebhookResult(keyName, url, new Exception("Failed request"));
        } catch (Exception e) {
            result = newFailedTriggerWebhookResult(keyName, url, e);
        } finally {
            sendWebhookEnd(url, content, keyName, startTime, result);
        }
        return result;
    }

    private void sendWebhookEnd(String url, String webContent, String keyName, Instant startTime, TriggerWebhookResult sr) {
        Instant endTime = Instant.now();
        long executeTime = Duration.between(startTime, endTime).toMillis();

        if (!sr.isSuccess()) {
            LOGGER.log(Level.WARNING, "send webhook failed, startTime={0}, endTime={1}, executeTime={2}ms, webHookURL={3}, req={4}, messageID={5}, user={6}, err={7}",
                    new Object[]{startTime, endTime, executeTime, url, webContent, messageID, user, sr.getErrMsg()});
        } else {
            LOGGER.log(Level.INFO, "send webhook succeed, startTime={0}, endTime={1}, executeTime={2}ms, webHookURL={3}, req={4}, messageID={5}, user={6}",
                    new Object[]{startTime, endTime, executeTime, url, webContent, messageID, user});
        }

        String triggerOn = option.getRecordActionTypeFrom();
        if (triggerOn == null || triggerOn.isEmpty()) {
            triggerOn = "send";
        }
        Tag resTag = sr.isSuccess() ? new Tag("result", "success") : new Tag("result", "failed");
        Tag webhookTypeTag = option.isFromDeprecatedSource() ? new Tag("webhook_type", "old_webhook") : new Tag("webhook_type", "new_webhook");

        Monitor.emitWebhookTimer(keyName, WEBHOOK_COST, executeTime, new Tag("trigger_on", triggerOn), webhookTypeTag, resTag);
        Monitor.emitWebhookTimer(TOTAL_WEBHOOK_PREFIX, WEBHOOK_COST, executeTime, new Tag("trigger_on", triggerOn), webhookTypeTag, resTag, new Tag("key_name", keyName));
    }


    private TriggerWebhookResult newSuccessTriggerWebhookResult(String webhookKeyName, String url) {
        return new TriggerWebhookResult(webhookKeyName, url, true, "");
    }

    private TriggerWebhookResult newFailedTriggerWebhookResult(String webhookKeyName, String url, Exception e) {
        return new TriggerWebhookResult(webhookKeyName, url, false, e.getMessage());
    }
}
