package org.example.buildjsonreq.models;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Title {
    @JsonProperty("i18n")
    private Lang i18n;

    @JsonProperty("tag")
    private String tag;

    public Title(Lang i18n, String tag) {
        this.i18n = i18n;
        this.tag = tag;
    }
}