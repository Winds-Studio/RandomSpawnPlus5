package systems.kscott.randomspawnplus.util;

import java.util.concurrent.ThreadLocalRandom;

public class Numbers {
    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        return ThreadLocalRandom.current().nextInt((max - min) + 1) + min;
    }

    public static boolean betweenExclusive(int x, int min, int max) {
        return x > min && x < max;
    }
}
