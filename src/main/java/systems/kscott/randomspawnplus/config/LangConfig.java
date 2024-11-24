package systems.kscott.randomspawnplus.config;

import io.github.thatsmusic99.configurationmaster.api.ConfigFile;
import systems.kscott.randomspawnplus.RandomSpawnPlus;

import java.io.File;

public class LangConfig {

    private static ConfigFile configFile;

    public String playerOnly;
    public String errorOnFindingSpawn;
    public String noSpawnFound;
    public String invalidPlayer;

    public String wildTp = "&a&l[!] &7You've been teleported to &b%x&7, &b%y&7, &b%z&7.";
    public String wildTpOther = "&a&l[!] &b%player&7 has been successfully teleported.";
    public String wildTpNonExist = "&c&l[!] &7That player doesn't exist.";
    public String wildTpCooldown = "&c&l[!] &7You must wait&c%delay&7 to use that command.";
    public String wildNoMoney = "&c[!] &7You don't have enough money to use &b/wild&7!";

    public String delayDay = "day";
    public String delayDays = "days";
    public String delayHour = "hour";
    public String delayHours = "hours";
    public String delayMin = "minute";
    public String delayMins = "minutes";
    public String delaySec = "second";
    public String delaySecs = "seconds";

    public LangConfig(boolean init, String fileName) throws Exception {
        RandomSpawnPlus instance = RandomSpawnPlus.getInstance();
        configFile = ConfigFile.loadConfig(new File(instance.getDataFolder(), fileName));

        // Add config header
        addCommentRegionBased("general",
                Config.getGlobalConfigHeader() +
                        "\n" +
                        "\n"
        );

        // Pre-structure to force order
        structureConfig();

        // Init value for config keys
        initConfig();
    }

    private void initConfig() {
        final String generalPath = "general.";
        final String commandPath = "command.";
        final String delayPath = "delay.";

        wildTp = getString(commandPath + "wild-tp", wildTp, "Placeholders: %x% | %y% | %z%");
        wildTpOther = getString(commandPath + "wild-tp-other", wildTpOther, "Placeholders: %player%");
        wildTpNonExist = getString(commandPath + "wild-tp-doesnt-exist", wildTpNonExist);
        wildTpCooldown = getString(commandPath + "wild-tp-cooldown", wildTpCooldown, "Placeholders: %delay%");
        wildNoMoney = getString(commandPath + "wild-no-money", wildNoMoney);

        delayDay = getString(delayPath + "day", delayDay);
        delayDays = getString(delayPath + "days", delayDays);
        delayHour = getString(delayPath + "hour", delayHour);
        delayHours = getString(delayPath + "hours", delayHours);
        delayMin = getString(delayPath + "minute", delayMin);
        delayMins = getString(delayPath + "minutes", delayMins);
        delaySec = getString(delayPath + "second", delaySec);
        delaySecs = getString(delayPath + "seconds", delaySecs);
    }

    public void saveConfig() throws Exception {
        configFile.save();
    }

    private void structureConfig() {
        createTitledSection("General", "general");
        createTitledSection("Command", "command");
        createTitledSection("Delay", "delay");
    }

    private void createTitledSection(String title, String path) {
        configFile.addSection(title);
        configFile.addDefault(path, null);
    }

    private String getString(String path, String def, String comment) {
        configFile.addDefault(path, def, comment);
        return configFile.getString(path, def);
    }

    private String getString(String path, String def) {
        configFile.addDefault(path, def);
        return configFile.getString(path, def);
    }

    private String getString(String path) {
        return configFile.getString(path, null);
    }

    private void addComment(String path, String comment) {
        configFile.addComment(path, comment);
    }

    private void addCommentRegionBased(String path, String... comments) {
        configFile.addComment(path, comments[0]); // TODO
    }
}
