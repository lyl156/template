package org.example.canary;

public class Example {
    /**
     * Determines if the given environment is allowed based on the rollout configuration.
     *
     * @param env the environment to check
     * @return true if the environment is allowed, false otherwise
     */
    public static boolean isXXEnv(String env) {
        return Rollout.isAllowed("xx_rollout_by_env",
                Rollout.useAccessListMethod(env),
                Rollout.usePercentageMethod(env));
    }

    /**
     * Determines if the given demand-side POC is allowed based on the rollout configuration.
     *
     * @param demandSidePOC the demand-side POC to check
     * @return true if the demand-side POC is allowed, false otherwise
     */
    public static boolean isXXlPOC(String demandSidePOC) {
        return Rollout.isAllowed("xx_rollout_by_poc",
                Rollout.useAccessListMethod(demandSidePOC),
                Rollout.usePercentageMethod(demandSidePOC));
    }
}
