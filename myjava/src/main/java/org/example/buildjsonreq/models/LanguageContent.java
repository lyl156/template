package org.example.buildjsonreq.models;


public class LanguageContent {
    private String text1;
    private String text2;
    private String button;
    private String url;

    public LanguageContent(String text1, String text2, String button, String url) {
        this.text1 = text1;
        this.text2 = text2;
        this.button = button;
        this.url = url;
    }

    public String getText1() {
        return text1;
    }

    public String getText2() {
        return text2;
    }

    public String getButton() {
        return button;
    }

    public String getUrl() {
        return url;
    }
}