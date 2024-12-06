package systems.kscott.randomspawnplus.platforms;

import it.unimi.dsi.fastutil.longs.LongArrayList;

public class UniversalPlatform {

    // Spawning status
    private static LongArrayList pendChunkGens;
    private static boolean isAllSpawnRangeChunksGenerated;

    // Check whether need to use method, or public field to access directly.
    // Or move to interface as default?
    public static void setPendingGenerateChunksList(LongArrayList chunks) {
        pendChunkGens = chunks;
    }

    public static LongArrayList getPendingGenerateChunksList() {
        return pendChunkGens;
    }

    public static void finalizeSpawnChunksGeneration() {
        isAllSpawnRangeChunksGenerated = true;
    }

    public static boolean isAllSpawnRangeChunksGenerated() {
        return isAllSpawnRangeChunksGenerated;
    }
}
