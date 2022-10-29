package systems.kscott.randomspawnplus.util;

import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import systems.kscott.randomspawnplus.RandomSpawnPlus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class ConfigFile {

    @Getter
    private FileConfiguration config;
    private RandomSpawnPlus plugin;
    private String fileName;

    public ConfigFile(RandomSpawnPlus plugin, String fileName) {
        this.fileName = fileName;
        this.plugin = plugin;
        reload();
    }

    private File createFile() {
        File customConfigFile = new File(plugin.getDataFolder(), fileName);
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            plugin.saveResource(fileName, false);
        }
        return customConfigFile;
    }

    public void reload() {
        File customConfigFile = createFile();
        config = new YamlConfiguration();
        try {
            config.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            config.save(Paths.get(plugin.getDataFolder().getAbsolutePath(), fileName).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
