package systems.kscott.randomspawnplus.util;

import org.bukkit.command.CommandSender;

public class MessageUtil {

    public static void broadcastCommandMessage(CommandSender sender, Object message) {
        PlatformUtil.getPlatform().broadcastCommandMessage(sender, message);
    }
}
