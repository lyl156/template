package __retry

import (
	"context"
	"reflect"
	"testing"

	"github.com/avast/retry-go"
	"github.com/stretchr/testify/assert"
)

func TestDoWithData(t *testing.T) {
	type args[T any] struct {
		f       func(spyAttempt *int) func() (T, error)
		options []retry.Option
	}
	type testCase[T any] struct {
		name         string
		args         args[T]
		want         T
		wantAttempts int
		wantErr      assert.ErrorAssertionFunc
	}
	tests := []testCase[int]{
		{
			name: "no errors",
			args: args[int]{
				f: func(spyAttempt *int) func() (int, error) {
					return func() (int, error) {
						*spyAttempt++
						return 1337, nil
					}
				},
				options: []retry.Option{retry.Attempts(5)},
			},
			want:         1337,
			wantAttempts: 1,
			wantErr: func(t assert.TestingT, err error, i ...interface{}) bool {
				return err == nil
			},
		},
		{
			name: "error for 2 iterations",
			args: args[int]{
				f: func(spyAttempt *int) func() (int, error) {
					return func() (int, error) {
						*spyAttempt++
						if *spyAttempt == 2 {
							return 666, nil
						}
						return 0, assert.AnError
					}
				},
				options: []retry.Option{retry.Attempts(5)},
			},
			want:         666,
			wantAttempts: 2,
			wantErr: func(t assert.TestingT, err error, i ...interface{}) bool {
				return err == nil
			},
		},
		{
			name: "errors for all attempts",
			args: args[int]{
				f: func(spyAttempt *int) func() (int, error) {
					return func() (int, error) {
						*spyAttempt++
						return 0, assert.AnError
					}
				},
				options: []retry.Option{retry.Attempts(5)},
			},
			want:         0,
			wantAttempts: 5,
			wantErr: func(t assert.TestingT, err error, i ...interface{}) bool {
				return err != nil
			},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			var wantAttempt int
			got, err := DoWithData(tt.args.f(&wantAttempt), tt.args.options...)
			assert.True(t, tt.wantErr(t, err))
			if !reflect.DeepEqual(got, tt.want) {
				t.Errorf("DoWithData() got = %v, want %v", got, tt.want)
			}
		})
	}
}

func TestDefaultRetryOptionsWithContext(t *testing.T) {
	// We should have some options here
	assert.Greater(t, len(DefaultRetryOptionsWithContext(context.TODO())), 0)
}
