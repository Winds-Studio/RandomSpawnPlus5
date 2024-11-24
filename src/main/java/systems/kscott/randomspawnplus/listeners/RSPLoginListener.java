package systems.kscott.randomspawnplus.listeners;

import systems.kscott.randomspawnplus.platforms.UniversalPlatform;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.ArrayList;
import java.util.UUID;

public class RSPLoginListener implements Listener {

    public static ArrayList<UUID> firstJoinPlayers = new ArrayList<>();
    private final FileConfiguration config;

    @EventHandler
    public void preLoginHandler(AsyncPlayerPreLoginEvent event) {
        if (!config.getBoolean("randomspawn-enabled")) {
            return;
        }

        if (!config.getBoolean("on-first-join")) {
            return;
        }

        // TODO: denied message
        if (!UniversalPlatform.isAllSpawnRangeChunksGenerated()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "RandomSpawnPlus WIP");
        }

        final UUID uuid = event.getUniqueId();
        final boolean hasPlayed = Bukkit.getServer().getOfflinePlayer(uuid).hasPlayedBefore();

        if (!hasPlayed) {
            firstJoinPlayers.add(uuid);
        }
    }
}
