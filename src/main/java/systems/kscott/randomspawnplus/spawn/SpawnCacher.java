package systems.kscott.randomspawnplus.spawn;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import systems.kscott.randomspawnplus.RandomSpawnPlus;
import systems.kscott.randomspawnplus.util.Locations;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SpawnCacher {

    public static SpawnCacher INSTANCE;

    private boolean spawnsRequireSaving;
    private final List<String> cachedSpawns;
    private WrappedTask cacheSpawnTask;

    public SpawnCacher() {
        this.spawnsRequireSaving = false;
        this.cachedSpawns = new ArrayList<>();
        cacheSpawns();
    }

    public static void initialize() {
        INSTANCE = new SpawnCacher();
    }

    public static SpawnCacher getInstance() {
        return INSTANCE;
    }

    private void cacheSpawns() {
        SpawnFinder finder = SpawnFinder.getInstance();

        List<String> locationStrings = spawns.getStringList("spawns");

        cachedSpawns.addAll(locationStrings);

        int missingLocations = config.getInt("spawn-cache-target") - locationStrings.size();

        if (missingLocations <= 0) {
            return;
        }

        List<String> newLocations = new ArrayList<>();

        Bukkit.getLogger().info("Caching " + missingLocations + " spawns.");
        for (int i = 0; i <= missingLocations; i++) {
            RandomSpawnPlus.getInstance().foliaLib.getImpl().runLater(() -> {
                Location location = null;
                boolean valid = false;

                while (!valid) {
                    location = finder.getCandidateLocation();
                    valid = finder.checkSpawn(location);
                }

                newLocations.add(Locations.serializeString(location));
            }, 1);
        }

        cacheSpawnTask = RandomSpawnPlus.getInstance().foliaLib.getImpl().runTimer(() -> {
            /* Wait for all spawns to be cached */
            if (newLocations.size() <= missingLocations) {
                if (RandomSpawnPlus.getInstance().getConfig().getBoolean("debug-mode")) {
                    System.out.println(newLocations.size() + ", " + missingLocations);
                }
            } else {
                cachedSpawns.addAll(newLocations);
                /* Save spawns to file */
                save();
                RandomSpawnPlus.getInstance().foliaLib.getImpl().cancelTask(cacheSpawnTask);
            }
        }, 10, 10);
    }

    public Location getRandomSpawn() {
        int element = ThreadLocalRandom.current().nextInt(cachedSpawns.size());
        return Locations.deserializeLocationString(cachedSpawns.get(element));
    }

    public void deleteSpawn(Location location) {
        cachedSpawns.removeIf(locationString -> Locations.serializeString(location).equals(locationString));
        spawnsRequireSaving = true;
    }

    public void save() {
        RandomSpawnPlus.getInstance().getSpawnsManager().getConfig().set("spawns", cachedSpawns);
        RandomSpawnPlus.getInstance().getSpawnsManager().save();
    }
}
