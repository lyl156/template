package org.example.canary;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RolloutTest {

    private static final String TEST_KEY = "test_key";

    @BeforeAll
    public static void setup() {
        // Simulate the dynamic configuration retrieval
        // Example configuration:
        // {
        //     "accessList": ["hello"],
        //     "blockList": ["rest"],
        //     "percentage": 50
        // }
        // This setup can involve initializing configurations or other necessary setups.
    }

    @Test
    public void testInvalidKey() {
        boolean result = Rollout.isAllowed("invalid",
                Rollout.useAccessListMethod("hello"),
                Rollout.usePercentageMethod("test,this")
        );
        assertFalse(result, "Expected rollout to be disallowed for invalid key.");
    }

    @Test
    public void testValidKeyButRolloutMethodsAreFalse() {
        boolean result = Rollout.isAllowed(TEST_KEY,
                Rollout.useAccessListMethod("invalid"),
                Rollout.usePercentageMethod("test,this")
        );
        assertFalse(result, "Expected rollout to be disallowed when rollout methods evaluate to false.");
    }

    @Test
    public void testValidKeyAndFirstRolloutMethodIsTrue() {
        boolean result = Rollout.isAllowed(TEST_KEY,
                Rollout.useAccessListMethod("hello"),
                Rollout.usePercentageMethod("invalid")
        );
        assertTrue(result, "Expected rollout to be allowed when the first rollout method evaluates to true.");
    }

    @Test
    public void testValidKeyButFirstRolloutIsFalseSecondIsTrue() {
        boolean result = Rollout.isAllowed(TEST_KEY,
                Rollout.useAccessListMethod("invalid"),
                Rollout.usePercentageMethod("test_key")
        );
        assertTrue(result, "Expected rollout to be allowed when the second rollout method evaluates to true.");
    }

    @Test
    public void testValidKeyButMiddleRolloutMethodMustNotRollout() {
        boolean result = Rollout.isAllowed(TEST_KEY,
                Rollout.useAccessListMethod("hello"),
                Rollout.useBlockListMethod("rest"),
                Rollout.usePercentageMethod("test,this")
        );
        assertFalse(result, "Expected rollout to be disallowed due to block list method.");
    }

    @Test
    public void testDefaultMethods() {
        Rollout.Method[] methods = Rollout.defaultMethods("stuff");
        assertEquals(2, methods.length, "Expected default methods to return two methods.");
    }
}
