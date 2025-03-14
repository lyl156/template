package org.example.buildjsonreq.models;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Lang {
    @JsonProperty("en_us")
    private String en;

    @JsonProperty("zh_cn")
    private String zh;

    public Lang(String en, String zh) {
        this.en = en;
        this.zh = zh;
    }
}