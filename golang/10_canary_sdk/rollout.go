package _0_canary_sdk

import (
	"context"
	"encoding/json"
)

// Config refers to the dynamic conf for rolling out a feature.
type Config struct {
	AccessList   []string `json:"accesslist,omitempty"`
	BlockList    []string `json:"blocklist,omitempty"`
	Percentage   int      `json:"percentage,omitempty"`
	All          bool     `json:"all,omitempty"`
	mustNotAllow bool     // Use to shortcircuit evaluation to false. This is for non-tolerable Method.
}

// Method represents the method used for the rollout.
type Method func(config *Config) bool

// UseAccessListMethod returns true if ALL items matches the access list configured.
func UseAccessListMethod(items ...string) Method {
	return func(config *Config) bool {
		if config.mustNotAllow {
			return false
		}
		if len(config.AccessList) > 0 && ContainsAll(config.AccessList, items...) {
			return true
		}
		return false
	}
}

// UseAccessListMethodAny returns true if one of items matches the access list configured.
func UseAccessListMethodAny(items ...string) Method {
	return func(config *Config) bool {
		if config.mustNotAllow || len(config.AccessList) == 0 {
			return false
		}

		for _, item := range items {
			if Contains(config.AccessList, item) {
				return true
			}
		}

		return false
	}
}

// UseBlockListMethod returns false if ANY item matches the block list configured.
// Should this rollout method evaluate to false, the rollout will not be allowed.
// Note: ANY was chosen to be more restricted against the items we want to block.
func UseBlockListMethod(items ...string) Method {
	return func(config *Config) bool {
		if config.mustNotAllow {
			return false
		}
		if len(config.BlockList) > 0 && ContainsAny(config.BlockList, items...) {
			config.mustNotAllow = true
			return false
		}
		return true
	}
}

// UsePercentageMethod returns true if the rollout lies within the percentage configured.
func UsePercentageMethod(value string) Method {
	return func(config *Config) bool {
		if config.mustNotAllow {
			return false
		}
		return CompareRatio(value, config.Percentage)
	}
}

// IsAllowed returns false if none of the methods for rollout evaluates to true.
// Additionally, as long as Config.mustNotAllow is set to true, this function will return false.
// When Config.mustNotAllow is false, as long as 1 Method evaluates to true this function returns true.
// Usage:
//
//	canRollout := rollout.IsAllowed("test_cc_key",
//		rollout.UseAccessListMethod(sendItem.Psm),
//		rollout.UsePercentageMethod("test_cc_key"+sendItem.Psm),
//		rollout.UseBlockListMethod("bad PSM"))
//	if canRollout {
//		...
//	}
func IsAllowed(key string, methods ...Method) bool {
	if key == "" {
		return false
	}

	ccConfig, err := GetDynamicConfig(context.Background(), key)
	if err != nil {
		return false
	}

	var config Config
	if err := json.Unmarshal([]byte(ccConfig), &config); err != nil {
		return false
	}

	var allow bool
	for _, method := range methods {
		if ok := method(&config); ok {
			allow = true
		}
	}
	return allow && !config.mustNotAllow
}

// DefaultMethods returns the most common rollout methods.
// Rollout methods are percentage and access list methods.
func DefaultMethods(value string) []Method {
	return []Method{
		UsePercentageMethod(value),
		UseAccessListMethod(value),
	}
}

func GetDynamicConfig(ctx context.Context, key string) (string, error) {
	//e.g.
	//{
	//    "accesslist": ["avc", "bcvc],
	//    "percentage": 0
	//}
	return "", nil
}
