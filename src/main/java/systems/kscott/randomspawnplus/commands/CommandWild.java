package systems.kscott.randomspawnplus.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import com.earth2me.essentials.User;
import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import systems.kscott.randomspawnplus.RandomSpawnPlus;
import systems.kscott.randomspawnplus.events.RandomSpawnEvent;
import systems.kscott.randomspawnplus.events.SpawnType;
import systems.kscott.randomspawnplus.exceptions.FinderTimedOutException;
import systems.kscott.randomspawnplus.spawn.SpawnFinder;
import systems.kscott.randomspawnplus.util.Chat;
import systems.kscott.randomspawnplus.util.CooldownManager;

import java.time.Instant;

@CommandAlias("wild|rtp")
@Description("Teleport to a random location")
public class CommandWild extends BaseCommand {

    private RandomSpawnPlus plugin;
    private FileConfiguration config;

    public CommandWild(RandomSpawnPlus plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @Default
    @CommandPermission("randomspawnplus.wild")
    public void wild(CommandSender sender) {
        if (!(sender instanceof Player)) {
            Chat.msg(sender, Chat.get("console-cannot-use"));
            return;
        }

        Player player = (Player) sender;

        long cooldown = CooldownManager.getCooldown(player);


        if (player.hasPermission("randomspawnplus.wild.bypasscooldown")) {
            cooldown = 0;
        }

        if ((cooldown - Instant.now().toEpochMilli()) >= 0) {
            if (config.getBoolean("debug-mode"))
                plugin.getLogger().info(Long.toString(cooldown));


            String message = plugin.getLang().getString("wild-tp-cooldown");
            message = message.replace("%delay", Chat.timeLeft(cooldown / 1000 - Instant.now().getEpochSecond()));

            Chat.msg(player, message);
            return;

        }
        if (plugin.getEconomy() != null && config.getInt("wild-cost") != 0) {
            if (!player.hasPermission("randomspawnplus.wild.bypasscost")) {
                if (plugin.getEconomy().has(player, config.getInt("wild-cost"))) {
                    plugin.getEconomy().withdrawPlayer(player, config.getInt("wild-cost"));
                } else {
                    Chat.msg(player, plugin.getLang().getString("wild-no-money"));
                    return;
                }
            }
        }

        Location location;
        try {
            location = SpawnFinder.getInstance().findSpawn(true);
        } catch (FinderTimedOutException e) {
            Chat.msg(player, Chat.get("error-finding-spawn"));
            return;
        }


        String message = Chat.get("wild-tp")
                .replace("%x", Integer.toString(location.getBlockX()))
                .replace("%y", Integer.toString(location.getBlockY()))
                .replace("%z", Integer.toString(location.getBlockZ()));
        Chat.msg(player, message);

        if (config.getBoolean("home-on-wild")) {
            User user = plugin.getEssentials().getUser(player);
            if (!user.hasHome()) {
                user.setHome("home", location);
                user.save();
            }
        }

        RandomSpawnEvent randomSpawnEvent = new RandomSpawnEvent(location, player, SpawnType.WILD_COMMAND);

        Bukkit.getServer().getPluginManager().callEvent(randomSpawnEvent);
        PaperLib.teleportAsync(player, location.add(0.5, 0, 0.5));
        CooldownManager.addCooldown(player);
    }

    @Default
    @CommandPermission("randomspawnplus.wild.others")
    public void wildOther(CommandSender sender, String otherPlayerString) {

        Player otherPlayer = Bukkit.getPlayer(otherPlayerString);

        if (otherPlayer == null) {
            Chat.msg(sender, plugin.getLang().getString("invalid-player"));
            return;
        }

        Location location;
        try {
            location = SpawnFinder.getInstance().findSpawn(true);
        } catch (FinderTimedOutException e) {
            Chat.msg(otherPlayer, Chat.get("error-finding-spawn"));
            return;
        }
        String message = Chat.get("wild-tp")
                .replace("%x", Integer.toString(location.getBlockX()))
                .replace("%y", Integer.toString(location.getBlockY()))
                .replace("%z", Integer.toString(location.getBlockZ()));

        Chat.msg(otherPlayer, message);

        message = Chat.get("wild-tp-other");
        message = message.replace("%player", otherPlayer.getName());
        Chat.msg(sender, message);

        RandomSpawnEvent randomSpawnEvent = new RandomSpawnEvent(location, otherPlayer.getPlayer(), SpawnType.WILD_COMMAND);

        Bukkit.getServer().getPluginManager().callEvent(randomSpawnEvent);
        if (location.isChunkLoaded()) {
            location.getChunk().load();
        }
        PaperLib.teleportAsync(otherPlayer, location.add(0.5, 0, 0.5));
    }
}
