package systems.kscott.randomspawnplus.listeners;

import systems.kscott.randomspawnplus.RandomSpawnPlus;
import systems.kscott.randomspawnplus.config.Config;
import systems.kscott.randomspawnplus.events.RandomSpawnEvent;
import systems.kscott.randomspawnplus.events.SpawnType;
import systems.kscott.randomspawnplus.spawn.SpawnFinder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RSPDeathListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerRespawnEvent event) {
        if (!Config.getGlobalConfig().randomSpawnOnDeath) {
            return;
        }

        Player player = event.getPlayer();

        if (player.isDead()) {
            if (!config.getBoolean("use-permission-node") || (config.getBoolean("use-permission-node") && player.hasPermission("randomspawnplus.randomspawn"))) {
                if (Config.getGlobalConfig().randomSpawnAtBed) {
                    if (player.getBedSpawnLocation() != null) {
                        event.setRespawnLocation(player.getBedSpawnLocation());
                        return;
                    }
                }

                Location location;

                try {
                    location = SpawnFinder.getInstance().findSpawn(true).add(0.5, 0, 0.5);
                } catch (Exception e) {
                    RandomSpawnPlus.getInstance().getLogger().warning("The spawn finder failed to find a valid spawn, and has not given " + player.getName() + " a random spawn. If you find this happening a lot, then raise the 'spawn-finder-tries-before-timeout' key in the config.");
                    return;
                }

                RandomSpawnEvent randomSpawnEvent = new RandomSpawnEvent(location, player, SpawnType.ON_DEATH);

                Bukkit.getServer().getPluginManager().callEvent(randomSpawnEvent);
                event.setRespawnLocation(location);
            }
        }
    }
}
