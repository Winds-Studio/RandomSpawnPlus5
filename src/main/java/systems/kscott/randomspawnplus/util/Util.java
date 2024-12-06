package systems.kscott.randomspawnplus.util;

import systems.kscott.randomspawnplus.RandomSpawnPlus;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Util {

    public static List<UUID> firstJoinPlayers = new ArrayList<>();
    public static Map<String, Long> cooldowns = new HashMap<>();

    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        return ThreadLocalRandom.current().nextInt((max - min) + 1) + min;
    }

    public static boolean betweenExclusive(int x, int min, int max) {
        return x > min && x < max;
    }

    public static void addCooldown(Player p) {
        int cooldown = RandomSpawnPlus.getInstance().getConfig().getInt("wild-cooldown");
        long now = Instant.now().toEpochMilli();
        long future = now + TimeUnit.SECONDS.toMillis(cooldown);

        cooldowns.put(p.getUniqueId().toString(), future);
    }

    public static long getCooldown(Player p) {
        if (!cooldowns.containsKey(p.getUniqueId().toString())) {
            return 0;
        }

        return cooldowns.get(p.getUniqueId().toString());
    }

    public static boolean doesClassExists(String clazz) {
        try {
            Class.forName(clazz);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
