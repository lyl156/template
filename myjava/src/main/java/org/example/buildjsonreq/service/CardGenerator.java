package org.example.buildjsonreq.service;


import org.example.buildjsonreq.models.*;
import org.example.buildjsonreq.utils.JsonUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CardGenerator {

    public static String generateCardContent(CardContent content) {
        Header header = buildHeader2Applier(content);
        Elements i18nElements = buildI18nElements(content);

        PostBody body = new PostBody(new Config(), header, i18nElements);
        return JsonUtil.toJsonString(body);
    }

    private static Header buildHeader2Applier(CardContent content) {
        String titleCN = "我是中文标题";
        String titleEN = "English header";


        Lang i18n = new Lang(titleEN, titleCN);
        Title title = new Title(i18n, "plain_text");
        return new Header("indigo", title);
    }

    public static Elements buildI18nElements(CardContent content) {
        String text1CN = "名称: " + content.name;
        String text1EN = "name: " + content.name;

        String text2CN;
        String text2EN;

        if (content.userEmails != null && !content.userEmails.isEmpty()) {
            text2CN = "人: " + content.nodeName + " " + user2MarkdownAt(content.users);
            text2EN = "user: " + content.nodeName + " " + user2MarkdownAt(content.userEmails);
        } else {
            text2CN = "人: " + content.nodeName + " " + String.join(", ", content.users);
            text2EN = "user: " + content.nodeName + " " + String.join(", ", content.userEmails);
        }

        String buttonCN = "详情";
        String buttonEN = "detail";

        Map<String, LanguageContent> languages = Map.of(
                "zh_cn", new LanguageContent(text1CN, text2CN, buttonCN, getUrl(content.url)),
                "en_us", new LanguageContent(text1EN, text2EN, buttonEN, getUrl(content.url))
        );

        return new Elements(
                generateLanguageElements(languages.get("zh_cn")),
                generateLanguageElements(languages.get("en_us"))
        );
    }

    private static List<Element> generateLanguageElements(LanguageContent langContent) {
        return Arrays.asList(
                Element.GenerateTextElement(langContent.getText1()),
                Element.GenerateTextElement(langContent.getText2()),
                Element.GenerateButtonElement(langContent.getButton(), langContent.getUrl())
        );
    }

    private static String user2MarkdownAt(List<String> emails) {
        StringBuilder sb = new StringBuilder();
        for (String email : emails) {
            sb.append("<at email=\"").append(email).append("\"></at>");
        }
        return sb.toString().trim();
    }

    private static String getUrl(URLData url) {
        return url.urlOnline; // 默认返回线上 URL
    }
}