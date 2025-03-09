package org.example.webhooktrigger;

class TriggerWebhookResult {
    private String webhookKeyName;
    private String url;
    private boolean isSuccess;
    private String errMsg;

    public TriggerWebhookResult(String webhookKeyName, String url, boolean isSuccess, String errMsg) {
        this.webhookKeyName = webhookKeyName;
        this.url = url;
        this.isSuccess = isSuccess;
        this.errMsg = errMsg;
    }

    public boolean isSuccess() { return isSuccess; }
    public boolean isFailed() { return !isSuccess; }
}
