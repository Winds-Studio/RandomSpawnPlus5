package systems.kscott.randomspawnplus.events;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SpawnCheckEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    @Getter
    private Location location;
    @Getter
    private boolean valid;
    @Getter
    private String validReason = "UNK";

    public SpawnCheckEvent(Location location) {
        this.location = location;
        this.valid = true;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }


    public void setValid(boolean valid, String reason) {
        this.validReason = reason;
        this.valid = valid;
    }


}
