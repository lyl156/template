package org.example.buildjsonreq.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Header {
    @JsonProperty("template")
    private String template;

    @JsonProperty("title")
    private Title title;

    @JsonProperty("tag")
    private String tag;


    public Header(String template, Title title) {
        this.template = template;
        this.title = title;
    }
}