package systems.kscott.randomspawnplus.util;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import systems.kscott.randomspawnplus.RandomSpawnPlus;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageUtil {

    public static void broadcastCommandMessage(CommandSender sender, Object message) {
        PlatformUtil.getPlatform().broadcastCommandMessage(sender, message);
    }

    public static void send(Player player, String... messages) {
        for (String message : messages) {
            RandomSpawnPlus.getInstance().adventure().player(player).sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
        }
    }

    public static void send(CommandSender sender, String... messages) {
        for (String message : messages) {
            RandomSpawnPlus.getInstance().adventure().sender(sender).sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
        }
    }

    public static String timeLeft(long seconds) {
        long days = seconds / 86400L;
        long hours = seconds / 3600L % 24L;
        long minutes = seconds / 60L % 60L;
        long secs = seconds % 60L;

        return (days > 0L ? " " + days + " " + (days != 1 ? get("delay.days") : get("delay.day")) : "")
                + (hours > 0L ? " " + hours + " " + (hours != 1 ? get("delay.hours") : get("delay.hour")) : "")
                + (minutes > 0L ? " " + minutes + " " + (minutes != 1 ? get("delay.minutes") : get("delay.minute")) : "")
                + (secs > 0L ? " " + secs + " " + (secs != 1 ? get("delay.seconds") : get("delay.second")) : "");
    }
}
