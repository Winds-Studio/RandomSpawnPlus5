package systems.kscott.randomspawnplus.config;

import io.github.thatsmusic99.configurationmaster.api.ConfigFile;
import io.github.thatsmusic99.configurationmaster.api.ConfigSection;
import systems.kscott.randomspawnplus.RandomSpawnPlus;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GlobalConfig {

    private static ConfigFile configFile;

    public boolean randomSpawnEnabled = true;
    public String respawnWorld = "world";
    public int spawnCacheCount = 150;
    public int spawnCacheTimeout = 1000;
    public int spawnFindingFailedThreshold = 50;

    public int spawnRangeMinX = -1000;
    public int spawnRangeMaxX = 1000;
    public int spawnRangeMinZ = -1000;
    public int spawnRangeMaxZ = 1000;

    public boolean blockedSpawnZoneEnabled = true;
    public int blockedSpawnZoneMinX = -100;
    public int blockedSpawnZoneMaxX = 100;
    public int blockedSpawnZoneMinZ = -100;
    public int blockedSpawnZoneMaxZ = 100;

    public boolean randomSpawnOnDeath = true;
    public boolean randomSpawnOnFirstJoin = true;
    public boolean randomSpawnAtBed = true;
    public List<String> unsafeBlocks = new ArrayList<>(Arrays.asList(
            "#VINE", "#AIR", "WATER", "LAVA" // TODO Note: Add implementation for #Liquid
    ));

    public String homeIntegrationMode = "essentialsx"; // TODO Note: Use enum
    public boolean useHomeOnDeath = true; // TODO Note: override on-death
    public boolean setHomeOnFirstJoinSpawn = true; // TODO Note: override on-first-join

    public boolean wildEnabled = true;
    public int wildCooldown = 300;
    public int wildDelay = 0;
    public boolean wildAllowFirstUseForAll = false;
    public int wildCost = 100;
    public boolean setHomeOnWild = false;

    public GlobalConfig(boolean init, String fileName) throws Exception {
        RandomSpawnPlus instance = RandomSpawnPlus.getInstance();
        configFile = ConfigFile.loadConfig(new File(instance.getDataFolder(), fileName));

        final String lastVersion = getString("config-version");

        // Set current config version
        configFile.set("config-version", Config.getCurrentConfigVersion());

        // Get config last version for upgrade task
        Config.getConfigVersion(lastVersion);

        // Add config header
        addCommentRegionBased("config-version",
                Config.getGlobalConfigHeader() +
                        "\n" +
                        "### NOTE: When modifying values such as spawn-range, and the cache is enabled, you'll need to reset spawns.yml.\n" +
                        "### This can be accomplished by simply deleting the spawns.yml and restarting the server.\n" +
                        "\n" +
                        "# Don't touch this!\n"
        );

        // Pre-structure to force order
        structureConfig();

        // Init value for config keys
        initConfig();

        // Validate values and disable plugin directly.
        validateConfigValues();
    }

    private void initConfig() {
        final String generalPath = "general.";
        final String spawnControlPath = "spawn-control.";
        final String hooksPath = "hooks.";
        final String wildCommandPath = "wild-command.";

        randomSpawnEnabled = getBoolean(generalPath + "random-spawn-enabled", randomSpawnEnabled, "Enable the random spawning feature? (disable this if you just want /wild)");
        respawnWorld = getString(generalPath + "respawn-world", respawnWorld, "Which world to respawn players in?");
        spawnCacheCount = getInt(generalPath + "spawn-cache-count", spawnCacheCount, "How many spawn locations should RandomSpawnPlus aim to keep cached?");
        spawnCacheTimeout = getInt(generalPath + "spawn-cache-timeout", spawnCacheTimeout, "How long the spawn locations cache should be refreshed?");
        spawnFindingFailedThreshold = getInt(generalPath + "spawn-finding-failed-threshold", spawnFindingFailedThreshold, "How many tries to find a valid spawn before the plugin times out?");

        final String spawnRangePath = generalPath + "spawn-range.";
        spawnRangeMinX = getInt(spawnRangePath + "min-x", spawnRangeMinX);
        spawnRangeMaxX = getInt(spawnRangePath + "max-x", spawnRangeMaxX);
        spawnRangeMinZ = getInt(spawnRangePath + "min-z", spawnRangeMinZ);
        spawnRangeMaxZ = getInt(spawnRangePath + "max-z", spawnRangeMaxZ);

        final String blockedSpawnZonePath = generalPath + "blocked-spawn-zone.";
        blockedSpawnZoneEnabled = getBoolean(blockedSpawnZonePath + "enabled", blockedSpawnZoneEnabled);
        blockedSpawnZoneMinX = getInt(blockedSpawnZonePath + "min-x", blockedSpawnZoneMinX);
        blockedSpawnZoneMaxX = getInt(blockedSpawnZonePath + "max-x", blockedSpawnZoneMaxX);
        blockedSpawnZoneMinZ = getInt(blockedSpawnZonePath + "min-z", blockedSpawnZoneMinZ);
        blockedSpawnZoneMaxZ = getInt(blockedSpawnZonePath + "max-z", blockedSpawnZoneMaxZ);

        randomSpawnOnDeath = getBoolean(spawnControlPath + "on-death", randomSpawnOnDeath);
        randomSpawnOnFirstJoin = getBoolean(spawnControlPath + "on-first-join", randomSpawnOnFirstJoin);
        randomSpawnAtBed = getBoolean(spawnControlPath + "spawn-at-bed", randomSpawnAtBed);
        unsafeBlocks = getList(spawnControlPath + "unsafe-blocks", unsafeBlocks);

        homeIntegrationMode = getString(hooksPath + "home-integration-mode", homeIntegrationMode);
        useHomeOnDeath = getBoolean(hooksPath + "use-home-on-death", useHomeOnDeath);
        setHomeOnFirstJoinSpawn = getBoolean(hooksPath + "set-home-first-join-random-spawn", setHomeOnFirstJoinSpawn);

        wildEnabled = getBoolean(wildCommandPath + "wild-enabled", wildEnabled);
        wildCooldown = getInt(wildCommandPath + "wild-cooldown", wildCooldown);
        wildDelay = getInt(wildCommandPath + "wild-delay", wildDelay);
        wildAllowFirstUseForAll = getBoolean(wildCommandPath + "wild-allow-first-use-no-permission", wildAllowFirstUseForAll);
        wildCost = getInt(wildCommandPath + "wild-cost", wildCost);
        setHomeOnWild = getBoolean(wildCommandPath + "set-home-on-wild", setHomeOnWild);
    }

    private void validateConfigValues() {
        List<String> errors = new ArrayList<>();

        if (RandomSpawnPlus.getInstance().getServer().getWorld(respawnWorld) == null) {
            errors.add("");
        }

        // Collect, print then throw error
        for (String error: errors) {
            RandomSpawnPlus.LOGGER.error(error);
        }

        throw new RuntimeException();
    }

    public void saveConfig() throws Exception {
        configFile.save();
    }

    private void structureConfig() {
        createTitledSection("General", "general");
        createTitledSection("Random Spawn Control", "spawn-control");
        createTitledSection("Hooks", "hooks");
        createTitledSection("/wild", "wild-command");
    }

    private void createTitledSection(String title, String path) {
        configFile.addSection(title);
        configFile.addDefault(path, null);
    }

    private boolean getBoolean(String path, boolean def, String comment) {
        configFile.addDefault(path, def, comment);
        return configFile.getBoolean(path, def);
    }

    private boolean getBoolean(String path, boolean def) {
        configFile.addDefault(path, def);
        return configFile.getBoolean(path, def);
    }

    private String getString(String path, String def, String comment) {
        configFile.addDefault(path, def, comment);
        return configFile.getString(path, def);
    }

    private String getString(String path, String def) {
        configFile.addDefault(path, def);
        return configFile.getString(path, def);
    }

    private double getDouble(String path, double def, String comment) {
        configFile.addDefault(path, def, comment);
        return configFile.getDouble(path, def);
    }

    private double getDouble(String path, double def) {
        configFile.addDefault(path, def);
        return configFile.getDouble(path, def);
    }

    private int getInt(String path, int def, String comment) {
        configFile.addDefault(path, def, comment);
        return configFile.getInteger(path, def);
    }

    private int getInt(String path, int def) {
        configFile.addDefault(path, def);
        return configFile.getInteger(path, def);
    }

    private List<String> getList(String path, List<String> def, String comment) {
        configFile.addDefault(path, def, comment);
        return configFile.getStringList(path);
    }

    private List<String> getList(String path, List<String> def) {
        configFile.addDefault(path, def);
        return configFile.getStringList(path);
    }

    private ConfigSection getConfigSection(String path, Map<String, Object> defaultKeyValue, String comment) {
        configFile.addDefault(path, null, comment);
        configFile.makeSectionLenient(path);
        defaultKeyValue.forEach((string, object) -> configFile.addExample(path + "." + string, object));
        return configFile.getConfigSection(path);
    }

    private ConfigSection getConfigSection(String path, Map<String, Object> defaultKeyValue) {
        configFile.addDefault(path, null);
        configFile.makeSectionLenient(path);
        defaultKeyValue.forEach((string, object) -> configFile.addExample(path + "." + string, object));
        return configFile.getConfigSection(path);
    }

    private Boolean getBoolean(String path) {
        String value = configFile.getString(path, null);

        return value == null ? null : Boolean.parseBoolean(value);
    }

    private String getString(String path) {
        return configFile.getString(path, null);
    }

    private Double getDouble(String path) {
        String value = configFile.getString(path, null);

        return value == null ? null : Double.parseDouble(value); // TODO: Need to check whether need to handle NFE correctly
    }

    private Integer getInt(String path) {
        String value = configFile.getString(path, null);

        return value == null ? null : Integer.parseInt(value); // TODO: Need to check whether need to handle NFE correctly
    }

    private List<String> getList(String path) {
        return configFile.<String>getList(path, null);
    }

    // TODO, check
    private ConfigSection getConfigSection(String path) {
        configFile.addDefault(path, null);
        configFile.makeSectionLenient(path);
        //defaultKeyValue.forEach((string, object) -> configFile.addExample(path + "." + string, object));
        return configFile.getConfigSection(path);
    }

    private void addComment(String path, String comment) {
        configFile.addComment(path, comment);
    }

    private void addCommentRegionBased(String path, String... comments) {
        configFile.addComment(path, comments[0]); // TODO
    }
}
