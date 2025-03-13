package org.example.canary;

import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import java.util.zip.CRC32;

public class RatioComparator {
    private static final Logger logger = Logger.getLogger(RatioComparator.class.getName());
    private static final int MIN_RATIO = 0;
    private static final int MAX_RATIO = 100;

    public static boolean compareRatio(String input, int target) {
        if (target <= MIN_RATIO) {
            return false;
        }
        if (target >= MAX_RATIO) {
            return true;
        }
        int ratio = getRatioValue(input);
        boolean result = ratio <= target;
        logger.info(String.format("result = %b, hash = %d, target = %d, input = %s", result, ratio, target, input));

        return result;
    }

    private static int getRatioValue(String input) {
        CRC32 crc32 = new CRC32();
        crc32.update(input.getBytes(StandardCharsets.UTF_8));
        return (int) (crc32.getValue() % MAX_RATIO);
    }

    public static void main(String[] args) {
        System.out.println(compareRatio("test_input", 50));
    }
}
