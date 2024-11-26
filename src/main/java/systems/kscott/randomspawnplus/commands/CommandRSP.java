package systems.kscott.randomspawnplus.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import systems.kscott.randomspawnplus.RandomSpawnPlus;
import systems.kscott.randomspawnplus.util.MessageUtil;
import org.bukkit.command.CommandSender;

@CommandAlias("rsp|randomspawnplus")
@Description("Manage the plugin")
@CommandPermission("randomspawnplus.manage")
public class CommandRSP extends BaseCommand {

    @Default
    @Subcommand("help|h")
    public void _main(CommandSender player) {
        MessageUtil.send(player,
                "&8[&3RandomSpawnPlus&8] &7Running &bv" + RandomSpawnPlus.getInstance().getDescription().getVersion() + "&7, made with love &a:^)",
                "",
                "&b/rsp &8- &7The help menu.",
                "&b/rsp reload &8- &7Reload the plugin configuration.",
                "&b/wild &8- &7Randomly teleport yourself.",
                "&b/wild <other> &8- &7Randomly teleport another player.",
                "&7Need help? Check out &bhttps://github.com/Winds-Studio/RandomSpawnPlus5&7."
        );
    }

    @Subcommand("reload")
    public void _reload(CommandSender player) {
        RandomSpawnPlus.getInstance().getConfigManager().reload();
        RandomSpawnPlus.getInstance().getLangManager().reload();
        RandomSpawnPlus.getInstance().getSpawnsManager().reload();
        MessageUtil.send(player, "&8[&3RandomSpawnPlus&8] &7Reloaded &bconfig.yml&7, &blang.yml&7, and &bspawns.yml&7.");
    }
}
