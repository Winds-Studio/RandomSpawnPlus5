package systems.kscott.randomspawnplus.events;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RandomSpawnEvent extends Event {

    @Getter
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    @Getter
    private Location location;
    @Getter
    private Player player;
    @Getter
    private SpawnType spawnType;


    public RandomSpawnEvent(Location location, Player player, SpawnType spawnType) {
        this.location = location;
        this.player = player;
        this.spawnType = spawnType;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}
