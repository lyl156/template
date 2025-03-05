package _0_canary_sdk

func IsXXEnv(env string) bool {
	return IsAllowed("xx_rollout_by_env",
		UseAccessListMethod(env),
		UsePercentageMethod(env))
}

func IsXXlPOC(demandSidePOC string) bool {
	return IsAllowed("xx_rollout_by_poc",
		UseAccessListMethod(demandSidePOC),
		UsePercentageMethod(demandSidePOC))
}
