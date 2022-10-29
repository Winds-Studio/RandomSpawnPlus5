package systems.kscott.randomspawnplus.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class Locations {
    public static Location deserializeLocationString(String locString) {

        String[] array = locString.split("\\|");

        World world = Bukkit.getWorld(array[0]);

        int x = Integer.parseInt(array[1]);
        int y = Integer.parseInt(array[2]);
        int z = Integer.parseInt(array[3]);

        return new Location(world, x, y, z);
    }

    public static String serializeString(Location location) {
        String world = location.getWorld().getName();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        return world + "|" + x + "|" + y + "|" + z;
    }

    public static List<Location> deserializeLocationStringList(List<String> locStrings) {
        List<Location> locations = new ArrayList<>();

        for (String string : locStrings) {
            locations.add(deserializeLocationString(string));
        }

        return locations;
    }

    public static List<String> serializeStringList(List<Location> locations) {
        List<String> locStrings = new ArrayList<>();

        for (Location location : locations) {
            locStrings.add(serializeString(location));
        }

        return locStrings;
    }

}
