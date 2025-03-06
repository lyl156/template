package _5_build_json_req

func GenerateTextElement(content string) element {
	return element{
		Tag: "div",
		Text: text{
			Content: content,
			Tag:     "lark_md",
		},
	}
}

func GenerateButtonElement(buttonText, url string) element {
	return element{
		Tag: "action",
		Actions: []action{
			{
				Tag: "button",
				Text: text{
					Content: buttonText,
					Tag:     "plain_text",
				},
				Type: "primary",
				URL:  url,
			},
		},
	}
}

type lang struct {
	EN string `json:"en_us"`
	ZH string `json:"zh_cn"`
}

type title struct {
	I18n lang   `json:"i18n"`
	Tag  string `json:"tag"`
}

type header struct {
	Template string `json:"template"`
	Title    title  `json:"title"`
	Tag      string `json:"tag"`
}

type text struct {
	Content string `json:"content"`
	Tag     string `json:"tag"`
}

type action struct {
	Tag  string `json:"tag,omitempty"`
	Text text   `json:"text,omitempty"`
	Type string `json:"type,omitempty"`
	URL  string `json:"url,omitempty"`
}

type element struct {
	Tag     string   `json:"tag,omitempty"`
	Text    text     `json:"text,omitempty"`
	Actions []action `json:"actions,omitempty"`
}

type elements struct {
	Chinese []element `json:"zh_cn"`
	English []element `json:"en_us"`
}

type postBody struct {
	Config       config   `json:"config"`
	Header       header   `json:"header"`
	I18nElements elements `json:"i18n_elements"`
}

type config struct {
	WideScreenMode bool `json:"wide_screen_mode"`
}
