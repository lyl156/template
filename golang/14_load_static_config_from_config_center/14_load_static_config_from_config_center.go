package _4_load_static_config_from_config_center

import (
	"context"
	"encoding/json"
	"fmt"
	"log"

	"github.com/stretchr/testify/assert/yaml"
)

type ConfigCenter struct {
}

var configCenter *ConfigCenter

func (c *ConfigCenter) Get(ctx context.Context, key string) (string, error) {

	return "", nil
}

func LoadConfigFromTCC(ctx context.Context, configFile string) error {
	data, err := configCenter.Get(ctx, configFile)
	if err != nil {
		return fmt.Errorf("LoadConfig TccClient get, error: %s", err.Error())
	}

	if err = yaml.Unmarshal([]byte(data), &Conf); err != nil {
		return fmt.Errorf("LoadFromTCC unmarshal err: %s", err.Error())
	}

	if j, err := json.Marshal(&Conf); err == nil {
		s := string(j)
		log.Printf("get config from config center: %+v", s)
	} else {
		return fmt.Errorf("marshal config failed, err: %s", err.Error())
	}

	return nil
}

func GetConfigByFormType(ctx context.Context, formType FormType) []SomeConfig {
	log.Printf("GetConfigByFormType get formType: %s", formType)
	if formConfig, exists := Conf.FormTypes[string(formType)]; exists {
		log.Printf("GetConfigByFormType get config: %+v", formConfig)

		return formConfig.someConfigs
	}
	log.Printf("GetConfigByFormType given unknown formType")

	return nil
}

const LarkIntegrationKey = "xx_data.yaml"

func main() {
	LoadConfigFromTCC(context.Background(), LarkIntegrationKey)
	specificConfig := GetConfigByFormType(context.Background(), "formType")
	fmt.Println(specificConfig)
}
