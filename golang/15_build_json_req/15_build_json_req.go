package _5_build_json_req

import (
	"fmt"
	"strings"
)

type CardContent struct {
	Name       string
	NodeName   string
	User       []string
	UserEmails []string

	URL URL
}

type URL struct {
	URLOffline string
	URLOnline  string
}

func GenerateCardContent(content CardContent) string {
	curHeader := buildHeader2Applier(content)

	i18nElements := buildI18nElements(content)

	body := postBody{
		Config: config{
			WideScreenMode: true,
		},
		Header:       curHeader,
		I18nElements: i18nElements,
	}

	return ToJsonStr(body)
}

func buildHeader2Applier(content CardContent) header {
	titleCN := "我是中文标题"
	titleEN := "English header"

	return header{
		Template: "indigo",
		Title: title{
			I18n: lang{
				EN: titleEN,
				ZH: titleCN,
			},
			Tag: "plain_text",
		},
	}
}

type LanguageContent struct {
	Text1  string
	Text2  string
	Button string
	URL    string
}

func buildI18nElements(content CardContent) elements {
	text1CN := fmt.Sprintf("名称: %s", content.Name)
	text1EN := fmt.Sprintf("name: %s", content.Name)

	var (
		text2CN string
		text2EN string
	)
	if len(content.UserEmails) > 0 {
		text2CN = fmt.Sprintf("人: %s %s", content.NodeName, user2MarkdownAt(content.User))
		text2EN = fmt.Sprintf("user: %s %s", content.NodeName, user2MarkdownAt(content.UserEmails))
	} else {
		text2CN = fmt.Sprintf("人: %s %s", content.NodeName, content.User)
		text2EN = fmt.Sprintf("user: %s %s", content.NodeName, content.UserEmails)
	}

	buttonCN := "详情"
	buttonEN := "detail"

	languages := map[string]LanguageContent{
		"zh_cn": {Text1: text1CN, Text2: text2CN, Button: buttonCN, URL: getURL(content.URL)},
		"en_us": {Text1: text1EN, Text2: text2EN, Button: buttonEN, URL: getURL(content.URL)},
	}

	multiLangElements := elements{
		Chinese: generateLanguageElements(languages["zh_cn"]),
		English: generateLanguageElements(languages["en_us"]),
	}

	return multiLangElements
}

func generateLanguageElements(langContent LanguageContent) []element {
	return []element{
		GenerateTextElement(langContent.Text1),
		GenerateTextElement(langContent.Text2),
		GenerateButtonElement(langContent.Button, langContent.URL),
	}
}

func user2MarkdownAt(emails []string) string {
	var sb strings.Builder
	for _, email := range emails {
		sb.WriteString(fmt.Sprintf("<at email=\"%s\"></at>", email))
	}
	return sb.String()
}

func getURL(url URL) string {
	online := true
	if online {
		return url.URLOnline
	}

	return url.URLOffline
}
