package systems.kscott.randomspawnplus.util;

import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import systems.kscott.randomspawnplus.RandomSpawnPlus;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.NumberFormat;
import java.util.*;

public class Chat {
    /* Thanks splodge */

    @Setter
    private static FileConfiguration lang;

    public static void initialize(RandomSpawnPlus plugin) {
        lang = plugin.getLang();
    }

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static List<String> color(List<String> lore) {
        if (lore.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> newLore = new ArrayList<>();

        for (String l : lore) {
            newLore.add(color(l));
        }

        return newLore;
    }

    public static void msg(Player player, String... messages) {
        Arrays.stream(messages).forEach((s) -> {
            player.sendMessage(color(s));
        });
    }

    public static void msg(CommandSender sender, String... messages) {
        Arrays.stream(messages).forEach((s) -> {
            sender.sendMessage(color(s));
        });
    }

    public static void msgAll(String... messages) {
        Bukkit.getOnlinePlayers().stream().forEach((o) -> {
            Arrays.stream(messages).forEach((s) -> {
                o.sendMessage(color(s));
            });
        });
    }

    public static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(color(message));
    }

    public static String uppercaseFirst(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1, str.length()).toLowerCase();
    }

    public static int getRandomNumber(int min, int max) {
        return (new Random()).nextInt(max - min + 1);
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
        return (days > 0L ? " " + days + " " + (days != 1 ? lang.getString("delay.days") : lang.getString("delay.day")) : "")
                + (hours > 0L ? " " + hours + " " + (hours != 1 ? lang.getString("delay.hours") : lang.getString("delay.hour")) : "")
                + (minutes > 0L ? " " + minutes + " " + (minutes != 1 ? lang.getString("delay.minutes") : lang.getString("delay.minute")) : "")
                + (seconds > 0L ? " " + seconds + " " + (seconds != 1 ? lang.getString("delay.seconds") : lang.getString("delay.second")) : "");
    }

    public static String formatDoubleValue(double value) {
        NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return nf.format(value);
    }

    public static boolean isServerOnline(String address, int port) {
        try {
            Socket s = new Socket();
            s.connect(new InetSocketAddress(address, port), 10);
            s.close();
            return true;
        } catch (Exception var3) {
            return false;
        }
    }

    public static String get(String key) {
        return ChatColor.translateAlternateColorCodes('&', lang.getString(key));
    }

}
