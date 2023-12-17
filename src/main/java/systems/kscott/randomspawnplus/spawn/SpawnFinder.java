package systems.kscott.randomspawnplus.spawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import systems.kscott.randomspawnplus.RandomSpawnPlus;
import systems.kscott.randomspawnplus.events.SpawnCheckEvent;
import systems.kscott.randomspawnplus.util.Chat;
import systems.kscott.randomspawnplus.util.Numbers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SpawnFinder {

    public static SpawnFinder INSTANCE;
    public FileConfiguration config;
    ArrayList<Material> unsafeBlocks;

    public SpawnFinder() {
        this.config = RandomSpawnPlus.getInstance().getConfig();

        /* Setup safeblocks */
        List<String> unsafeBlockStrings;
        unsafeBlockStrings = config.getStringList("unsafe-blocks");

        unsafeBlocks = new ArrayList<>();
        for (String string : unsafeBlockStrings) {
            unsafeBlocks.add(Material.matchMaterial(string));
        }
    }

    public static void initialize() {
        INSTANCE = new SpawnFinder();
    }

    public static SpawnFinder getInstance() {
        return INSTANCE;
    }

    public Location getCandidateLocation() {
        String worldString = config.getString("respawn-world");

        if (worldString == null) {
            RandomSpawnPlus.getInstance().getLogger().severe("You've incorrectly defined the `respawn-world` key in the config.");
            RandomSpawnPlus.getInstance().getServer().getPluginManager().disablePlugin(RandomSpawnPlus.getInstance());
            return null;
        }

        World world = Bukkit.getWorld(worldString);

        if (world == null) {
            RandomSpawnPlus.getInstance().getLogger().severe("The world '" + worldString + "' is invalid. Please change the 'respawn-world' key in the config.");
            RandomSpawnPlus.getInstance().getServer().getPluginManager().disablePlugin(RandomSpawnPlus.getInstance());
            return null;
        }

        int minX = config.getInt("spawn-range.min-x");
        int minZ = config.getInt("spawn-range.min-z");
        int maxX = config.getInt("spawn-range.max-x");
        int maxZ = config.getInt("spawn-range.max-z");

        if (config.getBoolean("blocked-spawns-zone.enabled")) {
            int minXblocked = config.getInt("blocked-spawns-zone.min-x");
            int minZblocked = config.getInt("blocked-spawns-zone.min-z");
            int maxXblocked = config.getInt("blocked-spawns-zone.max-x");
            int maxZblocked = config.getInt("blocked-spawns-zone.max-z");

            SpawnRegion region1 = new SpawnRegion(minX, minXblocked, minZ, minZblocked);
            SpawnRegion region2 = new SpawnRegion(minXblocked, maxXblocked, minZblocked, maxZ - maxZblocked);
            SpawnRegion region3 = new SpawnRegion(maxXblocked, maxX, maxZblocked, maxX);
            SpawnRegion region4 = new SpawnRegion(minZblocked, maxZ - minZblocked, minZ + minXblocked, maxZ - minZblocked);

            SpawnRegion[] spawnRegions = new SpawnRegion[]{region1, region2, region3, region4};

            SpawnRegion region = spawnRegions[ThreadLocalRandom.current().nextInt(3)];

            minX = region.getMinX();
            minZ = region.getMinZ();
            maxX = region.getMaxX();
            maxZ = region.getMaxZ();
        }

        int candidateX = Numbers.getRandomNumberInRange(minX, maxX);
        int candidateZ = Numbers.getRandomNumberInRange(minZ, maxZ);
        int candidateY = getHighestY(world, candidateX, candidateZ);

        return new Location(world, candidateX, candidateY, candidateZ);
    }

    private Location getValidLocation(boolean useSpawnCaching) throws Exception {
        boolean useCache = config.getBoolean("enable-spawn-cacher");

        boolean valid = false;

        Location location = null;

        int tries = 0;
        while (!valid) {
            if (tries >= config.getInt("spawn-finder-tries-before-timeout")) {
                throw new Exception();
            }
            if (SpawnCacher.getInstance().getCachedSpawns().isEmpty()) {
                RandomSpawnPlus.getInstance().getLogger().severe(Chat.get("no-spawns-cached"));
            }
            if (useCache && useSpawnCaching && !SpawnCacher.getInstance().getCachedSpawns().isEmpty()) {
                location = SpawnCacher.getInstance().getRandomSpawn();
            } else {
                location = getCandidateLocation();
            }
            valid = checkSpawn(location);

            if (!valid && useCache && useSpawnCaching) {
                SpawnCacher.getInstance().deleteSpawn(location);
            }
            tries = tries + 1;
        }
        if (location == null) return null;
        return location;
    }

    public Location findSpawn(boolean useSpawnCaching) throws Exception {

        Location location = getValidLocation(useSpawnCaching);
        if (location == null) return null;

        if (config.getBoolean("debug-mode")) {
            Location locClone = location.clone();
            System.out.println(locClone.getBlock().getType());
            System.out.println(locClone.add(0, 1, 0).getBlock().getType());
            System.out.println(locClone.add(0, 1, 0).getBlock().getType());
            System.out.println("Spawned at " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
        }
        return location.add(0, 1, 0);
    }

    public boolean checkSpawn(Location location) {
        if (location == null) return false;

        boolean blockWaterSpawns = config.getBoolean("block-water-spawns");
        boolean blockLavaSpawns = config.getBoolean("block-lava-spawns");
        boolean debugMode = config.getBoolean("debug-mode");
        boolean blockedSpawnRange = config.getBoolean("blocked-spawns-zone.enabled");

        int blockedMaxX = config.getInt("blocked-spawns-zone.max-x");
        int blockedMinX = config.getInt("blocked-spawns-zone.min-x");
        int blockedMaxZ = config.getInt("blocked-spawns-zone.max-z");
        int blockedMinZ = config.getInt("blocked-spawns-zone.min-z");

        boolean isValid;

        Location locClone = location.clone();

        if (!location.getChunk().isLoaded()) {
            return false;
        }

        Block block0 = locClone.getBlock();
        Block block1 = locClone.add(0, 1, 0).getBlock();
        Block block2 = locClone.add(0, 1, 0).getBlock();

        SpawnCheckEvent spawnCheckEvent = new SpawnCheckEvent(location);

        Bukkit.getServer().getPluginManager().callEvent(spawnCheckEvent);

        isValid = spawnCheckEvent.isValid();

        if (!isValid) {
            if (debugMode) {
                System.out.println("Invalid spawn: " + spawnCheckEvent.getValidReason());
            }
        }

        if (blockedSpawnRange) {
            if (Numbers.betweenExclusive((int) location.getX(), blockedMinX, blockedMaxX)) {
                isValid = false;
            }
            if (Numbers.betweenExclusive((int) location.getZ(), blockedMinZ, blockedMaxZ)) {
                isValid = false;
            }
        }

        if (block0.getType().isAir()) {
            if (debugMode) {
                System.out.println("Invalid spawn: block0 isAir");
            }
            isValid = false;
        }

        if (!block1.getType().isAir() || !block2.getType().isAir()) {
            if (debugMode) {
                System.out.println("Invalid spawn: block1 or block2 !isAir");
            }
            isValid = false;
        }

        if (unsafeBlocks.contains(block1.getType())) {
            if (debugMode) {
                System.out.println("Invalid spawn: " + block1.getType() + " is not a safe block!");
            }
            isValid = false;
        }

        if (blockWaterSpawns) {
            if (block0.getType() == Material.WATER) {
                if (debugMode) {
                    System.out.println("Invalid spawn: blockWaterSpawns");
                }
                isValid = false;
            }
        }

        if (blockLavaSpawns) {
            if (block0.getType() == Material.LAVA) {
                if (debugMode) {
                    System.out.println("Invalid spawn: blockLavaSpawns");
                }
                isValid = false;
            }
        }

        return isValid;
    }

    public int getHighestY(World world, int x, int z) {
        int maxHeight = world.getMaxHeight();
        int minHeight = world.getMinHeight();

        for (int i = maxHeight; i >= minHeight; i--) {
            Location location = new Location(world, x, i, z);
            if (!location.getBlock().isEmpty()) {
                if (config.getBoolean("debug-mode")) {
                    System.out.println(i);
                }
                return i;
            }
        }
        return minHeight;
    }


}
