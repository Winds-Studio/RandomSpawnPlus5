package systems.kscott.randomspawnplus.util;

import org.bukkit.entity.Player;
import systems.kscott.randomspawnplus.RandomSpawnPlus;

import java.time.Instant;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class CooldownManager {
    public static HashMap<String, Long> cooldowns = new HashMap<>();

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
}
