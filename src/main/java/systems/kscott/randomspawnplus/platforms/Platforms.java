package systems.kscott.randomspawnplus.platforms;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public interface Platforms {

    void collectNonGeneratedChunks(World level, int minX, int maxX, int minZ, int maxZ);

    void loadPendingGenerateChunks(LongArrayList chunks);

    void broadcastCommandMessage(CommandSender sender, Object message);
}
