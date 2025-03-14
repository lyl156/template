package org.example.buildjsonreq.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Text {
    @JsonProperty("content")
    private String content;

    @JsonProperty("tag")
    private String tag;

    public Text(String content, String tag) {
        this.content = content;
        this.tag = tag;
    }
}