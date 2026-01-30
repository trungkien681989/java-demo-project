package core.util.scripting.io;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomGenerator {

    private RandomGenerator() {
    }

    public static int getInt() {
        return getInt(0, Integer.MAX_VALUE);
    }

    public static int getInt(int min, int max) {
        java.util.Random random = new java.util.Random();
        return random.nextInt(max - min) + min;
    }

    public static String getRandomNumberWithLength(int length) {
        String randomNumeric = RandomStringUtils.randomNumeric(length);
        while (randomNumeric.startsWith("-"))
            randomNumeric = RandomStringUtils.randomNumeric(length);
        return randomNumeric;
    }

    public static String randomStringWithLength(int length) {
        String randomString = RandomStringUtils.randomAlphabetic(length);
        return randomString;
    }
}
