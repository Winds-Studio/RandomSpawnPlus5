package systems.kscott.randomspawnplus.listeners;

import com.earth2me.essentials.User;
import systems.kscott.randomspawnplus.RandomSpawnPlus;
import systems.kscott.randomspawnplus.config.Config;
import systems.kscott.randomspawnplus.events.RandomSpawnEvent;
import systems.kscott.randomspawnplus.events.SpawnType;
import systems.kscott.randomspawnplus.spawn.SpawnFinder;
import systems.kscott.randomspawnplus.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class RSPFirstJoinListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFirstJoin(PlayerJoinEvent event) {
        if (!Config.getGlobalConfig().randomSpawnOnFirstJoin) {
            return;
        }

        Player player = event.getPlayer();

        if (!Util.firstJoinPlayers.contains(player.getUniqueId())) {
            return;
        }

        if (config.getBoolean("use-permission-node") && !player.hasPermission("randomspawnplus.randomspawn")) {
            Util.firstJoinPlayers.remove(player.getUniqueId());
            return;
        }

        try {
            Location spawnLoc = SpawnFinder.getInstance().findSpawn(true);
            // quiquelhappy start - Prevent essentials home replace
            boolean prevent = false;

            if (config.getBoolean("essentials-home-on-first-spawn") && RandomSpawnPlus.getHooks().getEssentials() != null) {
                User user = RandomSpawnPlus.getHooks().getEssentials().getUser(player);
                if (!user.hasHome()) {
                    user.setHome("home", spawnLoc);
                    user.save();
                } else {
                    prevent = true;
                }
            }

            if (!prevent) {
                RandomSpawnPlus.getInstance().foliaLib.getImpl().runLater(() -> {
                    RandomSpawnEvent randomSpawnEvent = new RandomSpawnEvent(spawnLoc, player, SpawnType.FIRST_JOIN);

                    Bukkit.getServer().getPluginManager().callEvent(randomSpawnEvent);
                    RandomSpawnPlus.getInstance().foliaLib.getImpl().teleportAsync(player, spawnLoc.add(0.5, 0, 0.5));
                }, 3);
            } else {
                RandomSpawnPlus.getInstance().getLogger().warning("The spawn finder prevented a teleport for " + player.getUniqueId() + ", since essentials sethome is enabled and the player already had a home (perhaps old player data?).");
            }
            // quiquelhappy end
        } catch (Exception e) {
            RandomSpawnPlus.getInstance().getLogger().warning("The spawn finder failed to find a valid spawn, and has not given " + player.getUniqueId() + " a random spawn. If you find this happening a lot, then raise the 'spawn-finder-tries-before-timeout' key in the config.");
            return;
        }

        Util.firstJoinPlayers.remove(player.getUniqueId());
    }
}
