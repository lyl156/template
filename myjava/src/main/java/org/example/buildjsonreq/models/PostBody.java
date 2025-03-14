package org.example.buildjsonreq.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostBody {
    @JsonProperty("config")
    private Config config;

    @JsonProperty("header")
    private Header header;

    @JsonProperty("i18n_elements")
    private Elements i18nElements;

    public PostBody(Config config, Header header, Elements i18nElements) {
        this.config = config;
        this.header = header;
        this.i18nElements = i18nElements;
    }
}