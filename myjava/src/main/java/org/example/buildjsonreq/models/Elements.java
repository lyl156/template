package org.example.buildjsonreq.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Elements {
    @JsonProperty("zh_cn")
    private List<Element> chinese;

    @JsonProperty("en_us")
    private List<Element> english;

    public Elements(List<Element> chinese, List<Element> english) {
        this.chinese = chinese;
        this.english = english;
    }
}