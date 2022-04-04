package io.github.indroDevTeam.indroMain.commands.rank;

import io.github.indroDevTeam.indroMain.ranks.UserRanks;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandSetNameColour extends SubCommand {
    @Override
    public String getName() {
        return "setnamecolour";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Create a new rank";
    }

    @Override
    public String getSyntax() {
        return "/rank create <rankID> <format> <intMaxHomes> <nextRank> <advancementGate>...";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        if (commandSender instanceof Player player) {
            if (args.length == 3 && player.isOp()) {
                Player target = Bukkit.getPlayer(args[1]);
                if (Bukkit.getServer().getOnlinePlayers().contains(target)) {
                    assert target != null;
                    UserRanks.setChatColor(player, args[2]);

                    player.sendMessage("Set " + target.getName() + "'s name colour to " + args[2]);
                    target.sendMessage("Your name colour's been changed!");
                }
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] strings) {
        ArrayList<String> arguments = new ArrayList<>();
        if (strings.length == 2) {
            for (Player player1: Bukkit.getOnlinePlayers()) {
                arguments.add(player1.getName());
            }
        } else if (strings.length == 3) {
            for (int i = 0; i < UserRanks.Colours.values().length; i++) {
                arguments.add(UserRanks.Colours.values()[i].toString());
            }
        }
        return arguments;
    }
}
