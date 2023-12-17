package systems.kscott.randomspawnplus;

import co.aikar.commands.PaperCommandManager;
import com.tcoded.folialib.FoliaLib;
import lombok.Getter;
import net.ess3.api.IEssentials;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import systems.kscott.randomspawnplus.commands.CommandRSP;
import systems.kscott.randomspawnplus.commands.CommandWild;
import systems.kscott.randomspawnplus.listeners.RSPDeathListener;
import systems.kscott.randomspawnplus.listeners.RSPFirstJoinListener;
import systems.kscott.randomspawnplus.listeners.RSPLoginListener;
import systems.kscott.randomspawnplus.spawn.SpawnCacher;
import systems.kscott.randomspawnplus.spawn.SpawnFinder;
import systems.kscott.randomspawnplus.util.Chat;
import systems.kscott.randomspawnplus.util.ConfigFile;

public final class RandomSpawnPlus extends JavaPlugin {

    private static RandomSpawnPlus INSTANCE;
    private BukkitAudiences adventure;
    public FoliaLib foliaLib = new FoliaLib(this);

    @Getter
    private ConfigFile configManager;
    @Getter
    private ConfigFile langManager;
    @Getter
    private ConfigFile spawnsManager;

    @Getter
    private static Economy economy = null;
    @Getter
    private LuckPerms luckPerms;

    public static RandomSpawnPlus getInstance() {
        return INSTANCE;
    }

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

        configManager = new ConfigFile("config.yml");
        langManager = new ConfigFile("lang.yml");
        spawnsManager = new ConfigFile("spawns.yml");

        Chat.setLang(langManager.getConfig());

        registerEvents();
        registerCommands();

        SpawnFinder.initialize();
        SpawnCacher.initialize();
        Chat.initialize();

        new Metrics(this, 6465);

        if (getServer().getPluginManager().getPlugin("LuckPerms") != null) {
            /* LuckPerms is installed */
            try {
                setupPermissions();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getLogger().warning("The LuckPerms API is not detected, so the 'remove-permission-on-first-use' config option will not be enabled.");
        }

        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            /* Vault is installed */
            try {
                setupEconomy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getLogger().warning("The Vault API is not detected, so /wild cost will not be enabled.");
        }
    }

    @Override
    public void onDisable() {
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
        SpawnCacher.getInstance().save();
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new RSPDeathListener(), this);
        getServer().getPluginManager().registerEvents(new RSPLoginListener(), this);
        getServer().getPluginManager().registerEvents(new RSPFirstJoinListener(), this);
    }

    public void registerCommands() {
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new CommandRSP());
        if (configManager.getConfig().getBoolean("wild-enabled")) {
            manager.registerCommand(new CommandWild());
        }
    }

    public IEssentials getEssentials() {
        return (IEssentials) getServer().getPluginManager().getPlugin("Essentials");
    }

    public @NotNull FileConfiguration getConfig() {
        return configManager.getConfig();
    }

    public FileConfiguration getLang() {
        return langManager.getConfig();
    }

    public FileConfiguration getSpawns() {
        return spawnsManager.getConfig();
    }

    private void setupPermissions() {
        RegisteredServiceProvider<LuckPerms> rsp = getServer().getServicesManager().getRegistration(LuckPerms.class);
        if (rsp != null) {
            luckPerms = rsp.getProvider();
        } else {
            luckPerms = null;
        }
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        return (rsp != null ? rsp.getProvider() : null) != null;
    }
}
