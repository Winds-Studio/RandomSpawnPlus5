package systems.kscott.randomspawnplus.platforms;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import systems.kscott.randomspawnplus.RandomSpawnPlus;
import systems.kscott.randomspawnplus.spawn.SpawnFinder;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class SpigotPlatform implements Platforms {

    @Override
    public void collectNonGeneratedChunks(World level, int minX, int maxX, int minZ, int maxZ) {
        int minChunkX = minX >> 4;
        int maxChunkX = maxX >> 4;
        int minChunkZ = minZ >> 4;
        int maxChunkZ = maxZ >> 4;

        LongArrayList chunks = new LongArrayList();

        for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                Chunk chunk = level.getChunkAt(chunkX, chunkZ, false);

                if (!chunk.isGenerated()) {
                    long chunkKey = (long) chunkX & 0xffffffffL | ((long) chunkZ & 0xffffffffL) << 32;
                    chunks.add(chunkKey);
                }
            }
        }

        if (!chunks.isEmpty()) {
            UniversalPlatform.setPendingGenerateChunksList(chunks);
        } else {
            UniversalPlatform.finalizeSpawnChunksGeneration();
        }
    }

    @Override
    public void loadPendingGenerateChunks(LongArrayList chunks) {
        for (long chunkKey : chunks) {
            int chunkX = (int) chunkKey;
            int chunkZ = (int) (chunkKey >>> 32);

            Runnable loadChunk = () -> SpawnFinder.spawnLevel.getChunkAt(chunkX, chunkZ, true);
            Bukkit.getScheduler().runTaskAsynchronously(RandomSpawnPlus.getInstance(), loadChunk);
        }
    }

    @Override
    public void broadcastCommandMessage(CommandSender sender, Object message) {
        throw new UnsupportedOperationException();
    }
}
