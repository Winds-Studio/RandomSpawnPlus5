package systems.kscott.randomspawnplus.config;

import io.github.thatsmusic99.configurationmaster.api.ConfigFile;
import io.github.thatsmusic99.configurationmaster.api.ConfigSection;
import systems.kscott.randomspawnplus.RandomSpawnPlus;

import java.io.File;
import java.util.List;
import java.util.Map;

public class GlobalConfig {

    private static ConfigFile configFile;

    public String a;

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
    }

    private static void initConfig() {

    }

    public void saveConfig() throws Exception {
        configFile.save();
    }

    private void structureConfig() {
        createTitledSection("General", "general");
        createTitledSection("Random Spawn", "spawn");
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
