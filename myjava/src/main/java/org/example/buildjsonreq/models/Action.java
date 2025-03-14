package org.example.buildjsonreq.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Action {
    @JsonProperty("tag")
    private String tag;

    @JsonProperty("text")
    private Text text;

    @JsonProperty("type")
    private String type;

    @JsonProperty("url")
    private String url;

    public Action(String buttonText, String url) {
        this.tag = "button";
        this.text = new Text(buttonText, "plain_text");
        this.type = "primary";
        this.url = url;
    }
}