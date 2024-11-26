package systems.kscott.randomspawnplus.hooks;

import net.ess3.api.IEssentials;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import systems.kscott.randomspawnplus.RandomSpawnPlus;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class HookInstance {

    private final RandomSpawnPlus pluginInstance;

    private IEssentials essentials;
    private LuckPerms luckPerms;
    private static Economy economy;

    public HookInstance(RandomSpawnPlus instance) {
        pluginInstance = instance;

        registerHooks();
    }

    private void registerHooks() {
        new Metrics(pluginInstance, 6465); // TODO Note: Use own bstats, since no one update.

        Plugin essPlugin = pluginInstance.getServer().getPluginManager().getPlugin("Essentials");

        if (essPlugin != null) {
            essentials = (IEssentials) essPlugin;
        }

        if (pluginInstance.getServer().getPluginManager().getPlugin("LuckPerms") != null) {
            try {
                setupPermissions();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            RandomSpawnPlus.LOGGER.warn("The LuckPerms API is not detected, so the 'remove-permission-on-first-use' config option will not be enabled.");
        }

        if (pluginInstance.getServer().getPluginManager().getPlugin("Vault") != null) {
            try {
                setupEconomy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            RandomSpawnPlus.LOGGER.warn("The Vault API is not detected, so /wild cost will not be enabled.");
        }
    }

    private void setupPermissions() {
        RegisteredServiceProvider<LuckPerms> rsp = pluginInstance.getServer().getServicesManager().getRegistration(LuckPerms.class);
        if (rsp != null) {
            luckPerms = rsp.getProvider();
        } else {
            luckPerms = null;
        }
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = pluginInstance.getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) return false;

        economy = rsp.getProvider();

        return economy != null;
    }

    public HookInstance getInstance() {
        return this;
    }

    public IEssentials getEssentials() {
        return essentials;
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }

    public Economy getEconomy() {
        return economy;
    }
}
