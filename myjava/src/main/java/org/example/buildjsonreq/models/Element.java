package org.example.buildjsonreq.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Element {
    @JsonProperty("tag")
    private String tag;

    @JsonProperty("text")
    private Text text;

    @JsonProperty("actions")
    private List<Action> actions;

    private Element(String tag, Text text, List<Action> actions) {
        this.tag = tag;
        this.text = text;
        this.actions = actions;
    }

    public static Element GenerateTextElement(String content) {
        return new Element("div", new Text(content, "lark_md"), null);
    }

    public static Element GenerateButtonElement(String buttonText, String url) {
        return new Element("action", null, List.of(new Action(buttonText, url)));
    }
}