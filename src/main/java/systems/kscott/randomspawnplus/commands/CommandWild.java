package systems.kscott.randomspawnplus.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import com.earth2me.essentials.User;
import systems.kscott.randomspawnplus.RandomSpawnPlus;
import systems.kscott.randomspawnplus.config.Config;
import systems.kscott.randomspawnplus.events.RandomSpawnEvent;
import systems.kscott.randomspawnplus.events.SpawnType;
import systems.kscott.randomspawnplus.spawn.SpawnFinder;
import systems.kscott.randomspawnplus.util.MessageUtil;
import systems.kscott.randomspawnplus.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;

@CommandAlias("wild|rtp")
@Description("Teleport to a random location")
public class CommandWild extends BaseCommand {

    @Default
    @CommandPermission("randomspawnplus.wild")
    public void wild(CommandSender sender) {
        if (!(sender instanceof Player)) {
            MessageUtil.send(sender, Config.getLangConfig().playerOnly);
            return;
        }

        Player player = (Player) sender;
        long cooldown = Util.getCooldown(player);

        if (player.hasPermission("randomspawnplus.wild.bypasscooldown")) {
            cooldown = 0;
        }

        if ((cooldown - Instant.now().toEpochMilli()) >= 0) {
            String message = Config.getLangConfig().wildTpCooldown;
            message = message.replace("%delay%", MessageUtil.timeLeft(cooldown / 1000 - Instant.now().getEpochSecond()));

            MessageUtil.send(player, message);
            return;
        }

        int wildCost = Config.getGlobalConfig().wildCost;

        if (wildCost != 0 && RandomSpawnPlus.getHooks().getEconomy() != null && !player.hasPermission("randomspawnplus.wild.bypasscost")) {
            if (RandomSpawnPlus.getHooks().getEconomy().has(player, wildCost)) {
                RandomSpawnPlus.getHooks().getEconomy().withdrawPlayer(player, wildCost);
            } else {
                MessageUtil.send(player, Config.getLangConfig().wildNoMoney);
                return;
            }
        }

        Location location;

        try {
            location = SpawnFinder.getInstance().findSpawn(true);
        } catch (Exception e) {
            MessageUtil.send(player, Config.getLangConfig().errorOnFindingSpawn);
            return;
        }

        String message = Config.getLangConfig().wildTp
                .replace("%x%", Integer.toString(location.getBlockX()))
                .replace("%y%", Integer.toString(location.getBlockY()))
                .replace("%z%", Integer.toString(location.getBlockZ()));

        MessageUtil.send(player, message);

        if (Config.getGlobalConfig().setHomeOnWild && RandomSpawnPlus.getHooks().getEssentials() != null) {
            User user = RandomSpawnPlus.getHooks().getEssentials().getUser(player);
            if (!user.hasHome()) {
                user.setHome("home", location);
                user.save();
            }
        }

        RandomSpawnEvent randomSpawnEvent = new RandomSpawnEvent(location, player, SpawnType.WILD_COMMAND);

        Bukkit.getServer().getPluginManager().callEvent(randomSpawnEvent);
        RandomSpawnPlus.getInstance().foliaLib.getImpl().teleportAsync(player, location.add(0.5, 0, 0.5));
        Util.addCooldown(player);
    }

    @Default
    @CommandPermission("randomspawnplus.wild.others")
    public void wildOther(CommandSender sender, String otherPlayerString) {
        Player otherPlayer = Bukkit.getPlayer(otherPlayerString);

        if (otherPlayer == null) {
            MessageUtil.send(sender, Config.getLangConfig().invalidPlayer);
            return;
        }

        Location location;

        try {
            location = SpawnFinder.getInstance().findSpawn(true);
        } catch (Exception e) {
            MessageUtil.send(otherPlayer, Config.getLangConfig().errorOnFindingSpawn);
            return;
        }

        String message = Config.getLangConfig().wildTp
                .replace("%x%", Integer.toString(location.getBlockX()))
                .replace("%y%", Integer.toString(location.getBlockY()))
                .replace("%z%", Integer.toString(location.getBlockZ()));

        MessageUtil.send(otherPlayer, message);

        message = Config.getLangConfig().wildTpOther.replace("%player%", otherPlayer.getName());

        MessageUtil.send(sender, message);

        RandomSpawnEvent randomSpawnEvent = new RandomSpawnEvent(location, otherPlayer.getPlayer(), SpawnType.WILD_COMMAND);

        Bukkit.getServer().getPluginManager().callEvent(randomSpawnEvent);

        if (!location.getChunk().isLoaded()) {
            location.getChunk().load();
        }

        RandomSpawnPlus.getInstance().foliaLib.getImpl().teleportAsync(otherPlayer, location.add(0.5, 0, 0.5));
    }
}
