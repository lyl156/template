package org.example.buildjsonreq.service;


import java.util.List;

public class CardContent {
    public String name;
    public String nodeName;
    public List<String> users;
    public List<String> userEmails;
    public URLData url;

    public CardContent(String name, String nodeName, List<String> users, List<String> userEmails, URLData url) {
        this.name = name;
        this.nodeName = nodeName;
        this.users = users;
        this.userEmails = userEmails;
        this.url = url;
    }
}