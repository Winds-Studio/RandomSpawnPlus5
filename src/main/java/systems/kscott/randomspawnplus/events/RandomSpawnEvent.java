package systems.kscott.randomspawnplus.events;

import org.jetbrains.annotations.NotNull;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RandomSpawnEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final Location location;
    private final Player player;
    private final SpawnType spawnType;


    public RandomSpawnEvent(Location location, Player player, SpawnType spawnType) {
        this.location = location;
        this.player = player;
        this.spawnType = spawnType;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}
