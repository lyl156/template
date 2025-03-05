package _0_canary_sdk

import (
	"os"
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestMain(m *testing.M) {
	//val:
	//{
	//    "accesslist": ["hello"],
	//    "blocklist": ["rest"],
	//    "percentage": 50
	//}
	os.Exit(m.Run())
}

func TestCanRollout(t *testing.T) {
	testKey := "test_key"
	type args struct {
		key     string
		methods []Method
	}
	tests := []struct {
		name string
		args args
		want bool
	}{
		{
			name: "invalid key",
			args: args{
				key: "invalid",
				methods: []Method{
					UseAccessListMethod("hello"),
					UsePercentageMethod("test,this"),
				},
			},
			want: false,
		},
		{
			name: "valid key but rollout methods are false",
			args: args{
				key: testKey,
				methods: []Method{
					UseAccessListMethod("invalid"),
					UsePercentageMethod("test,this"),
				},
			},
			want: false,
		},
		{
			name: "valid key and the first rollout method is true",
			args: args{
				key: testKey,
				methods: []Method{
					UseAccessListMethod("hello"),
					UsePercentageMethod("invalid"),
				},
			},
			want: true,
		},
		{
			name: "valid key but first rollout is false but second rollout is true",
			args: args{
				key: testKey,
				methods: []Method{
					UseAccessListMethod("hello"),
					UsePercentageMethod("valid"),
				},
			},
			want: true,
		},
		{
			name: "valid key but middle rollout method must not rollout",
			args: args{
				key: testKey,
				methods: []Method{
					UseAccessListMethod("hello"),
					UseBlockListMethod("rest"),
					UsePercentageMethod("test,this"),
				},
			},
			want: false,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := IsAllowed(tt.args.key, tt.args.methods...); got != tt.want {
				t.Errorf("IsAllowed() = %v, want %v", got, tt.want)
			}
		})
	}
}

func TestDefaultMethods(t *testing.T) {
	methods := DefaultMethods("stuff")
	assert.Equal(t, 2, len(methods))
}
