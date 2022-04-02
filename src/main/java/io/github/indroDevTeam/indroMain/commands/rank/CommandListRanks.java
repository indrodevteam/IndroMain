package io.github.indroDevTeam.indroMain.commands.rank;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.ranks.Rank;
import io.github.indroDevTeam.indroMain.ranks.RankStorage;
import io.github.indroDevTeam.indroMain.ranks.RankUtils;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CommandListRanks extends SubCommand {
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Lists all the ranks available, and if you have reached them";
    }

    @Override
    public String getSyntax() {
        return "/rank listranks";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        // initialise string messages
        final String achieved = ChatColor.GREEN + "Achieved!";
        final String notAchieved = ChatColor.RED + "Not Achieved!";

        ArrayList<Rank> rankList = RankStorage.findAllRanks();
        if (commandSender instanceof Player player) {
            player.sendMessage("***** RANK LIST *****");
            for (Rank rankListOption: rankList) {
                String result;
                if (RankUtils.criteriaFulfilled(player, rankListOption) == 0) {
                    result = achieved;
                } else {
                    result = notAchieved;
                }

                player.sendMessage(String.format("| Rank: %s - Achieved %s", rankListOption.getRankName(), result));
            }
            player.sendMessage("*** END RANK LIST ***");
        } else {
            commandSender.sendMessage("***** RANK LIST *****");
            for (Rank rankListOption: rankList) {
                commandSender.sendMessage(String.format("| Rank: %s - Achieved %s", rankListOption.getRankName(), notAchieved));
            }
            commandSender.sendMessage("*** END RANK LIST ***");
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] strings) {
        return null;
    }
}
