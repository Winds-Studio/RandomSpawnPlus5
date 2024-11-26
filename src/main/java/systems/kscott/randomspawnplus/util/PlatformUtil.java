package systems.kscott.randomspawnplus.util;

import systems.kscott.randomspawnplus.platforms.FoliaPlatform;
import systems.kscott.randomspawnplus.platforms.Platforms;
import systems.kscott.randomspawnplus.platforms.SpigotPlatform;

public class PlatformUtil {

    private static Platforms platform;

    public static Platforms init() {
        // Folia
        if (Util.doesClassExists("io.papermc.paper.threadedregions.RegionizedServer")) {
            return platform = new FoliaPlatform();
        }

        // Paper
        if (Util.doesClassExists("io.papermc.paper.configuration.GlobalConfiguration")) {
            return platform = new FoliaPlatform();
        }

        // Spigot (Fallback)
        return platform = new SpigotPlatform();
    }

    public static Platforms getPlatform() {
        return platform;
    }
}
