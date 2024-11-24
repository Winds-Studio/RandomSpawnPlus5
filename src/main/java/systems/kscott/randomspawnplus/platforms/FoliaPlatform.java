package systems.kscott.randomspawnplus.platforms;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class FoliaPlatform implements Platforms {

    @Override
    public void collectNonGeneratedChunks(World level, int minX, int maxX, int minZ, int maxZ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void loadPendingGenerateChunks(LongArrayList chunks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void broadcastCommandMessage(CommandSender sender, Object message) {
        throw new UnsupportedOperationException();
    }
}
