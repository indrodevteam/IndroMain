package io.github.indroDevTeam.indroMain.commands.rank;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.dataUtils.LanguageLoader;
import io.github.indroDevTeam.indroMain.ranks.Rank;
import io.github.indroDevTeam.indroMain.ranks.RankStorage;
import me.kodysimpson.simpapi.command.SubCommand;
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
        if (args.length == 3) {
            OfflinePlayer player = Bukkit.getPlayer(args[1]);
            HashMap<UUID, Rank> rankHashMap = IndroMain.getPlayerRankList();
            if (player == null) {
                sender.sendMessage("Player does not exist!");
                return;
            }

            Rank playerRank = rankHashMap.get(player.getUniqueId());
            Rank newPlayerRank = RankStorage.readRank(args[2]);
            assert newPlayerRank != null;
            rankHashMap.replace(player.getUniqueId(), newPlayerRank);
            sender.sendMessage("Changed " + playerRank.getRankName() + " to " + newPlayerRank.getRankName());
        } else {
            sender.sendMessage(new LanguageLoader().get("plugin-title") + new LanguageLoader().get("error-syntax"));
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
                arguments.add(rank.getRankName());
            }
        }
        return arguments;
    }
}
