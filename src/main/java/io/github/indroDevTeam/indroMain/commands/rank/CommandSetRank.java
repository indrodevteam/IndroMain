package io.github.indroDevTeam.indroMain.commands.rank;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.dataUtils.LanguageTags;
import io.github.indroDevTeam.indroMain.ranks.Rank;
import io.github.indroDevTeam.indroMain.ranks.RankStorage;
import io.github.indroDevTeam.indroMain.ranks.UserRanks;
import me.kodysimpson.simpapi.command.SubCommand;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CommandSetRank extends SubCommand {
    @Override
    public String getName() {
        return "set";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Allows you to change the rank of each user";
    }

    @Override
    public String getSyntax() {
        return "/rank set <player> <rankName>";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length == 3 && sender.isOp()) {
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage("Player does not exist!");
                return;
            }

            Rank playerRank = UserRanks.getRank(player);
            Rank newPlayerRank = RankStorage.readRank(args[2]);
            if (newPlayerRank == null) {
                sender.sendMessage("This rank does not exist!");
                return;
            }
            UserRanks.setRank(player, newPlayerRank);
            sender.sendMessage("Changed " + playerRank.getRankTag() + " to " + newPlayerRank.getRankTag());
        } else {
            sender.sendMessage(LanguageTags.ERROR_SYNTAX.get());
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        List<String> arguments = new ArrayList<>();
        if (args.length == 2) {
            for (Player player1 :
                    Bukkit.getServer().getOnlinePlayers()) {
                arguments.add(player1.getName());
            }
        }
        if (args.length == 3) {
            for (Rank rank :
                    RankStorage.findAllRanks()) {
                arguments.add(rank.getRankTag());
            }
        }
        return arguments;
    }
}
