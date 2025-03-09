package org.example.webhooktrigger;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;
import java.util.List;

public class TriggerData {
    @JsonProperty("send_alert")
    private boolean sendAlert;

    @JsonProperty("ack")
    private boolean ack;

    @JsonProperty("create_chat_group")
    private boolean createChatGroup;

    public boolean isSendAlert() {
        return sendAlert;
    }

    public void setSendAlert(boolean sendAlert) {
        this.sendAlert = sendAlert;
    }

    public boolean isAck() {
        return ack;
    }

    public void setAck(boolean ack) {
        this.ack = ack;
    }

    public boolean isCreateChatGroup() {
        return createChatGroup;
    }

    public void setCreateChatGroup(boolean createChatGroup) {
        this.createChatGroup = createChatGroup;
    }
}

class Webhook {
    @JsonProperty("id")
    private String id;

    @JsonProperty("key_name")
    private String keyName;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("last_updated_by")
    private String lastUpdatedBy;

    @JsonProperty("admins")
    private List<String> admins;

    @JsonProperty("type")
    private String type;

    @JsonProperty("url")
    private String url;

    @JsonProperty("http_method")
    private String httpMethod;

    @JsonProperty("description")
    private Description desc;

    @JsonProperty("desc")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String descOld;

    @JsonProperty("trigger")
    private TriggerData trigger;

    @JsonProperty("status")
    private String status;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("expire_time")
    private LocalDateTime expireTime;

    @JsonProperty("params")
    private List<WebhookParam> params;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public List<String> getAdmins() {
        return admins;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Description getDesc() {
        return desc;
    }

    public void setDesc(Description desc) {
        this.desc = desc;
    }

    public String getDescOld() {
        return descOld;
    }

    public void setDescOld(String descOld) {
        this.descOld = descOld;
    }

    public TriggerData getTrigger() {
        return trigger;
    }

    public void setTrigger(TriggerData trigger) {
        this.trigger = trigger;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public List<WebhookParam> getParams() {
        return params;
    }

    public void setParams(List<WebhookParam> params) {
        this.params = params;
    }
}

class Description {
    @JsonProperty("chinese")
    private String chinese;

    @JsonProperty("english")
    private String english;

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }
}

class WebhookParam {
    @JsonProperty("type")
    private String type;

    @JsonProperty("key")
    private String key;

    @JsonIgnore
    private String value;

    @JsonProperty("default_value")
    private String defaultValue;

    @JsonProperty("value_type")
    private String valueType;

    @JsonProperty("description")
    private Description desc;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public Description getDesc() {
        return desc;
    }

    public void setDesc(Description desc) {
        this.desc = desc;
    }
}
