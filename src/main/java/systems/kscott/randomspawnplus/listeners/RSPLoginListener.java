package systems.kscott.randomspawnplus.listeners;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import systems.kscott.randomspawnplus.RandomSpawnPlus;

import java.util.ArrayList;
import java.util.UUID;

public class RSPLoginListener implements Listener {

    public static ArrayList<UUID> firstJoinPlayers = new ArrayList<>();
    private FileConfiguration config;


    public RSPLoginListener(RandomSpawnPlus plugin) {
        this.config = plugin.getConfig();
    }

    @EventHandler
    public void preLoginHandler(AsyncPlayerPreLoginEvent event) {
        if (config.getBoolean("randomspawn-enabled")) {
            if (config.getBoolean("on-first-join")) {
                UUID playerUUID = event.getUniqueId();

                boolean hasPlayed = Bukkit.getServer().getOfflinePlayer(playerUUID).hasPlayedBefore();

                if (!hasPlayed) {
                    firstJoinPlayers.add(playerUUID);
                }
            }
        }
    }
}