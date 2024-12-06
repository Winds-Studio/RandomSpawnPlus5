package systems.kscott.randomspawnplus.listeners;

import systems.kscott.randomspawnplus.RandomSpawnPlus;
import systems.kscott.randomspawnplus.config.Config;
import systems.kscott.randomspawnplus.platforms.UniversalPlatform;
import systems.kscott.randomspawnplus.util.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class RSPLoginListener implements Listener {

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (!Config.getGlobalConfig().randomSpawnOnFirstJoin) {
            return;
        }

        if (!UniversalPlatform.isAllSpawnRangeChunksGenerated()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Config.getLangConfig().spawnNotInitialized);
        }

        final UUID uuid = event.getUniqueId();
        final boolean hasPlayed = RandomSpawnPlus.getInstance().getServer().getOfflinePlayer(uuid).hasPlayedBefore();

        if (!hasPlayed) {
            Util.firstJoinPlayers.add(uuid);
        }
    }
}
