package org.example.buildjsonreq.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Config {
    @JsonProperty("wide_screen_mode")
    private boolean wideScreenMode = true;
}