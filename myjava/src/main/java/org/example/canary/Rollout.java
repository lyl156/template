package org.example.canary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.function.Function;

public class Rollout {

    // Config refers to the dynamic conf for rolling out a feature.
    public static class Config {
        private List<String> accessList;
        private List<String> blockList;
        private int percentage;
        private boolean all;
        private boolean mustNotAllow; // Use to shortcircuit evaluation to false. This is for non-tolerable Method.

        // Getters and setters
        public List<String> getAccessList() {
            return accessList;
        }

        public void setAccessList(List<String> accessList) {
            this.accessList = accessList;
        }

        public List<String> getBlockList() {
            return blockList;
        }

        public void setBlockList(List<String> blockList) {
            this.blockList = blockList;
        }

        public int getPercentage() {
            return percentage;
        }

        public void setPercentage(int percentage) {
            this.percentage = percentage;
        }

        public boolean isAll() {
            return all;
        }

        public void setAll(boolean all) {
            this.all = all;
        }

        public boolean isMustNotAllow() {
            return mustNotAllow;
        }

        public void setMustNotAllow(boolean mustNotAllow) {
            this.mustNotAllow = mustNotAllow;
        }
    }

    // Method represents the method used for the rollout.
    @FunctionalInterface
    public interface Method extends Function<Config, Boolean> {
    }

    // UseAccessListMethod returns true if ALL items match the access list configured.
    public static Method useAccessListMethod(String... items) {
        return config -> {
            if (config.isMustNotAllow()) {
                return false;
            }
            if (config.getAccessList() != null && Contain.containsAll(config.getAccessList(), items)) {
                return true;
            }
            return false;
        };
    }

    // UseAccessListMethodAny returns true if one of the items matches the access list configured.
    public static Method useAccessListMethodAny(String... items) {
        return config -> {
            if (config.isMustNotAllow() || config.getAccessList() == null) {
                return false;
            }
            for (String item : items) {
                if (config.getAccessList().contains(item)) {
                    return true;
                }
            }
            return false;
        };
    }

    // UseBlockListMethod returns false if ANY item matches the block list configured.
    // Should this rollout method evaluate to false, the rollout will not be allowed.
    // Note: ANY was chosen to be more restricted against the items we want to block.
    public static Method useBlockListMethod(String... items) {
        return config -> {
            if (config.isMustNotAllow()) {
                return false;
            }
            if (config.getBlockList() != null && Contain.containsAny(config.getBlockList(), items)) {
                config.setMustNotAllow(true);
                return false;
            }
            return true;
        };
    }

    // UsePercentageMethod returns true if the rollout lies within the percentage configured.
    public static Method usePercentageMethod(String value) {
        return config -> {
            if (config.isMustNotAllow()) {
                return false;
            }
            return RatioComparator.compareRatio(value, config.getPercentage());
        };
    }

    // IsAllowed returns false if none of the methods for rollout evaluates to true.
    // Additionally, as long as Config.mustNotAllow is set to true, this function will return false.
    // When Config.mustNotAllow is false, as long as one Method evaluates to true this function returns true.
    public static boolean isAllowed(String key, Method... methods) {
        if (key == null || key.isEmpty()) {
            return false;
        }

        String ccConfig = getDynamicConfig(key);
        if (ccConfig == null) {
            return false;
        }

        Config config = parseConfig(ccConfig);
        if (config == null) {
            return false;
        }

        boolean allow = false;
        for (Method method : methods) {
            if (method.apply(config)) {
                allow = true;
            }
        }

        return allow && !config.isMustNotAllow();
    }

    // DefaultMethods returns the most common rollout methods.
    // Rollout methods are percentage and access list methods.
    public static Method[] defaultMethods(String value) {
        return new Method[]{
                usePercentageMethod(value),
                useAccessListMethod(value)
        };
    }

    // Simulated method to get dynamic config as a JSON string.
    private static String getDynamicConfig(String key) {
        // Example JSON configuration
        return "{\"accessList\": [\"avc\", \"bcvc\"], \"percentage\": 0}";
    }

    // Parses the JSON configuration into a Config object.
    private static Config parseConfig(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        // Configure the ObjectMapper to handle unknown properties gracefully
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(json, Config.class);
        } catch (JsonProcessingException e) {
            // Log the error and handle it appropriately
            System.err.println("Failed to parse JSON configuration: " + e.getMessage());
            // Depending on your application's requirements, you might want to return a default Config,
            // throw a custom exception, or handle the error in another way.
            return null;
        }
    }
}