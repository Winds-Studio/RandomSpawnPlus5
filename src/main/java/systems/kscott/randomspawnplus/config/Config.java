package systems.kscott.randomspawnplus.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import systems.kscott.randomspawnplus.util.MessageUtil;
import org.bukkit.command.CommandSender;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class Config {

    public static final Logger LOGGER = LogManager.getLogger(Config.class.getSimpleName());
    private static final String CURRENT_REGION = Locale.getDefault().getCountry().toUpperCase(Locale.ROOT);
    private static final String GLOBAL_CONFIG_FILE_NAME = "config.yml";
    private static final String LANG_CONFIG_FILE_NAME = "lang.yml";

    private static ConfigLocale configLocale;
    private static GlobalConfig globalConfig;
    private static LangConfig langConfig;

    private static final String currConfigVer = "3.0";
    private static int lastConfigVerMajor;
    private static int lastConfigVerMinor;
    private static final int currConfigVerMajor = Integer.parseInt(currConfigVer.split("\\.")[0]);
    private static final int currConfigVerMinor = Integer.parseInt(currConfigVer.split("\\.")[1]);

    @Contract(" -> new")
    public static @NotNull CompletableFuture<Void> reloadAsync(CommandSender sender) {
        return CompletableFuture.runAsync(() -> {
            long begin = System.nanoTime();

            try {
                loadGlobalConfig(false);
            } catch (Exception e) {
                MessageUtil.broadcastCommandMessage(sender, Component.text("Failed to reload " + GLOBAL_CONFIG_FILE_NAME + ". See error in console!", NamedTextColor.RED));
                LOGGER.error(e);
            }
            try {
                loadLangConfig(false);
            } catch (Exception e) {
                MessageUtil.broadcastCommandMessage(sender, Component.text("Failed to reload " + LANG_CONFIG_FILE_NAME + ". See error in console!", NamedTextColor.RED));
                LOGGER.error(e);
            }

            final String success = String.format("Successfully reloaded config in %sms.", (System.nanoTime() - begin) / 1_000_000);
            MessageUtil.broadcastCommandMessage(sender, Component.text(success, NamedTextColor.GREEN));
        });
    }

    public static void loadConfig() {
        long begin = System.nanoTime();
        LOGGER.info("Loading config...");

        try {
            loadGlobalConfig(true);
        } catch (Exception e) {
            LOGGER.error("Failed to load " + GLOBAL_CONFIG_FILE_NAME + "!", e);
        }
        try {
            loadLangConfig(true);
        } catch (Exception e) {
            LOGGER.error("Failed to load " + LANG_CONFIG_FILE_NAME + "!", e);
        }

        LOGGER.info("Successfully loaded config in {}ms.", (System.nanoTime() - begin) / 1_000_000);
    }

    private static void loadGlobalConfig(boolean init) throws Exception {
        globalConfig = new GlobalConfig(init, GLOBAL_CONFIG_FILE_NAME);

        globalConfig.saveConfig();
    }

    private static void loadLangConfig(boolean init) throws Exception {
        langConfig = new LangConfig(init, LANG_CONFIG_FILE_NAME);

        langConfig.saveConfig();
    }

    public static String getGlobalConfigHeader() {
        return "#############################\n" +
                "#      RandomSpawnPlus5     #\n" +
                "#       Version 6.0.0       #\n" +
                "#   by @89apt89 & @Dreeam   #\n" +
                "#############################\n";
    }

    public static GlobalConfig getGlobalConfig() {
        return globalConfig;
    }

    public static LangConfig getLangConfig() {
        return langConfig;
    }

    public static void getConfigVersion(String lastConfigVer) {
        lastConfigVerMajor = Integer.parseInt(lastConfigVer.split("\\.")[0]);
        lastConfigVerMinor = Integer.parseInt(lastConfigVer.split("\\.")[1]);
    }

    public static String getCurrentConfigVersion() {
        return currConfigVer;
    }

    private ConfigLocale getConfigLocale() {
        if (configLocale != null) {
            ;
            return configLocale;
        }

        ConfigLocale locale;
        switch (CURRENT_REGION) {
            case "EN": {
                locale = ConfigLocale.ENGLISH;
                break;
            }
            case "CN": {
                locale = ConfigLocale.CHINESE;
                break;
            }
            case "RU": {
                locale = ConfigLocale.RUSSIAN;
                break;
            }
            default: {
                locale = ConfigLocale.ENGLISH;
                break;
            }
        }

        configLocale = locale;
        return locale;
    }

    enum ConfigLocale {
        ENGLISH,
        CHINESE,
        RUSSIAN
    }
}
