package systems.kscott.randomspawnplus.util;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import systems.kscott.randomspawnplus.RandomSpawnPlus;
import systems.kscott.randomspawnplus.config.Config;
import systems.kscott.randomspawnplus.config.LangConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

public class Chat {
    /* Thanks splodge */

    private static LangConfig lang;

    public static void initialize() {
        lang = Config.getLangConfig();
    }

    public static void msg(Player player, String... messages) {
        Arrays.stream(messages).forEach((s) -> RandomSpawnPlus.getInstance().adventure().player(player).sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(s)));
    }

    public static void msg(CommandSender sender, String... messages) {
        Arrays.stream(messages).forEach((s) -> RandomSpawnPlus.getInstance().adventure().sender(sender).sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(s)));
    }

    public static void msgAll(String... messages) {
        Arrays.stream(messages).forEach((s) -> RandomSpawnPlus.getInstance().adventure().all().sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(s)));
    }

    public static String uppercaseFirst(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static String formatMs(long ms) {
        long seconds = ms / 1000L % 60L;
        long minutes = ms / 60000L % 60L;
        long hours = ms / 3600000L % 24L;
        return (hours > 0L ? hours + "h " : "") + (minutes > 0L ? minutes + "m " : "") + seconds + "s";
    }

    public static String timeLeft(long timeoutSeconds) {
        long days = timeoutSeconds / 86400L;
        long hours = timeoutSeconds / 3600L % 24L;
        long minutes = timeoutSeconds / 60L % 60L;
        long seconds = timeoutSeconds % 60L;
        return (days > 0L ? " " + days + " " + (days != 1 ? get("delay.days") : get("delay.day")) : "")
                + (hours > 0L ? " " + hours + " " + (hours != 1 ? get("delay.hours") : get("delay.hour")) : "")
                + (minutes > 0L ? " " + minutes + " " + (minutes != 1 ? get("delay.minutes") : get("delay.minute")) : "")
                + (seconds > 0L ? " " + seconds + " " + (seconds != 1 ? get("delay.seconds") : get("delay.second")) : "");
    }

    public static String formatDoubleValue(double value) {
        NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return nf.format(value);
    }

    public static String get(String key) {
        return null;
    }
}
