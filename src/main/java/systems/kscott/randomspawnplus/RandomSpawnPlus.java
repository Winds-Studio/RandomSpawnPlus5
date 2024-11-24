package systems.kscott.randomspawnplus;

import co.aikar.commands.PaperCommandManager;
import com.tcoded.folialib.FoliaLib;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import systems.kscott.randomspawnplus.commands.CommandRSP;
import systems.kscott.randomspawnplus.commands.CommandWild;
import systems.kscott.randomspawnplus.config.Config;
import systems.kscott.randomspawnplus.hooks.HookInstance;
import systems.kscott.randomspawnplus.listeners.RSPDeathListener;
import systems.kscott.randomspawnplus.listeners.RSPFirstJoinListener;
import systems.kscott.randomspawnplus.listeners.RSPLoginListener;
import systems.kscott.randomspawnplus.spawn.SpawnCacher;
import systems.kscott.randomspawnplus.spawn.SpawnFinder;
import systems.kscott.randomspawnplus.util.Chat;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class RandomSpawnPlus extends JavaPlugin {

    private static RandomSpawnPlus INSTANCE;
    private static HookInstance hookInstance;
    public static final Logger LOGGER = LogManager.getLogger(RandomSpawnPlus.class.getSimpleName());
    public FoliaLib foliaLib = new FoliaLib(this);

    private BukkitAudiences adventure;
    public @NotNull BukkitAudiences adventure() {
        if (this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    @Override
    public void onEnable() {

        INSTANCE = this;
        this.adventure = BukkitAudiences.create(this);

        Chat.setLang(langManager.getConfig());

        Config.loadConfig();

        registerEvents();
        registerCommands();
        registerHooks();

        SpawnFinder.init();
        SpawnCacher.initialize();
        Chat.initialize();
    }

    @Override
    public void onDisable() {
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }

        SpawnCacher.getInstance().save();
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new RSPDeathListener(), this);
        getServer().getPluginManager().registerEvents(new RSPLoginListener(), this);
        getServer().getPluginManager().registerEvents(new RSPFirstJoinListener(), this);
    }

    private void registerCommands() {
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new CommandRSP());
        if (configManager.get().getBoolean("wild-enabled")) {
            manager.registerCommand(new CommandWild());
        }
    }

    public static RandomSpawnPlus getInstance() {
        return INSTANCE;
    }

    private void registerHooks() {
        hookInstance = new HookInstance(this);
    }

    public static HookInstance getHooks() {
        return hookInstance.getInstance();
    }

    public @NotNull FileConfiguration getConfig() {
        return configManager.getConfig();
    }

    public FileConfiguration getSpawns() {
        return spawnsManager.getConfig();
    }
}
