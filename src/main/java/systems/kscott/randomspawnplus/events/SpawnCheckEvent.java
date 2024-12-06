package systems.kscott.randomspawnplus.events;

import org.jetbrains.annotations.NotNull;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SpawnCheckEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final Location location;
    private boolean valid;
    private String validReason = "Unknown";

    public SpawnCheckEvent(Location location) {
        this.location = location;
        this.valid = true;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS_LIST;
    }


    public void setValid(boolean valid, String reason) {
        this.validReason = reason;
        this.valid = valid;
    }
}
