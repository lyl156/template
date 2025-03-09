package org.example.webhooktrigger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.OutputStream;
import java.io.IOException;



public class Trigger {
    // TODO: builder pattern
    private Option option;
    private String user;
    private String messageID;

    public Trigger(String user, String messageID, Option option) {
        this.user = user;
        this.messageID = messageID;
        this.option = option;
    }

    public List<TriggerWebhookResult> triggerWebhook() {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        CompletionService<TriggerWebhookResult> completionService = new ExecutorCompletionService<>(executor);
        List<TriggerWebhookResult> results = new CopyOnWriteArrayList<>();
        List<Webhook> ruleWebhookList = new ArrayList<>(); // Mocked list
        String webhookContent = "this is content struct";

        for (Webhook webhook : ruleWebhookList) {
            completionService.submit(() -> sendWebhook(webhook.getUrl(), webhookContent, webhook.getKeyName()));
        }

        executor.shutdown();
        try {
            for (int i = 0; i < ruleWebhookList.size(); i++) {
                Future<TriggerWebhookResult> future = completionService.take();
                results.add(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    private TriggerWebhookResult sendWebhook(String url, String content, String keyName) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                os.write(content.getBytes());
                os.flush();
            }
            int responseCode = con.getResponseCode();
            return responseCode == 200 ? newSuccessTriggerWebhookResult(keyName, url) : newFailedTriggerWebhookResult(keyName, url, new Exception("Failed request"));
        } catch (IOException e) {
            return newFailedTriggerWebhookResult(keyName, url, e);
        }
    }

    private TriggerWebhookResult newSuccessTriggerWebhookResult(String webhookKeyName, String url) {
        return new TriggerWebhookResult(webhookKeyName, url, true, "");
    }

    private TriggerWebhookResult newFailedTriggerWebhookResult(String webhookKeyName, String url, Exception e) {
        return new TriggerWebhookResult(webhookKeyName, url, false, e.getMessage());
    }
}
