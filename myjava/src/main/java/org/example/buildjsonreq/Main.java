package org.example.buildjsonreq;

import org.example.buildjsonreq.service.CardContent;
import org.example.buildjsonreq.service.CardGenerator;
import org.example.buildjsonreq.service.URLData;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        CardContent content = new CardContent(
                "示例名称", "示例节点",
                List.of("用户A", "用户B"), List.of("usera@example.com", "userb@example.com"),
                new URLData("http://offline.url", "http://online.url")
        );

        String res = CardGenerator.generateCardContent(content);
        System.out.println(res);
    }
}
