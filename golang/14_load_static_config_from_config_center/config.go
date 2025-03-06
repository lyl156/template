package _4_load_static_config_from_config_center

var Conf Config

// Config 结构体，包含多个 FormType
type Config struct {
	FormTypes map[string]FormTypeConfig `yaml:"form_types"`
}

// FormTypeConfig 结构体，存储不同 FormType 的 config
type FormTypeConfig struct {
	someConfigs []SomeConfig `yaml:"some_configs"`
}

type SomeConfig struct {
	Text  string `yaml:"text" json:"text"`
	Value string `yaml:"value" json:"value"`
}

type FormType string

// form_types:
//  procurement_demand_form_type:
//    some_configs:
//      - text: "aa"
//        value: "vsdv"
//      - text: "bb"
//        value: "clfoe"
