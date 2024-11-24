package systems.kscott.randomspawnplus.util;

import systems.kscott.randomspawnplus.platforms.Platforms;

public class PlatformUtil {

    private static Platforms platform;
    public static boolean isFolia;

    public static void isFolia() {
        if (false) {
            isFolia = false;
            return;
        }

        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            isFolia = true;
        } catch (Exception e) {
            isFolia = false;
        }
    }

    public static Platforms getPlatform() {
        return platform;
    }
}
