package io.github.indroDevTeam.indroMain.commands.rank;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.ranks.Rank;
import io.github.indroDevTeam.indroMain.ranks.RankStorage;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandRankInfo extends SubCommand {
    @Override
    public String getName() {
        return "info";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Gets the data of your current rank, or the data of other ranks";
    }

    @Override
    public String getSyntax() {
        return "/rank info (rankName)";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof Player player) {
            Rank userRank = null;
            if (strings.length == 1) {
                userRank = IndroMain.getInstance().getRank(player);
                if (userRank == null) {
                    IndroMain.getPlayerRankList().replace(player.getUniqueId(), RankStorage.readRank("DEFAULT"));
                    userRank = IndroMain.getInstance().getRank(player);
                }
            } else if (strings.length == 2) {
                userRank = RankStorage.readRank(strings[1]);
            }

            if (userRank == null) {
                player.sendMessage("Rank does not exist!");
                return;
            }

            ArrayList<String> message = new ArrayList<>();
            message.add("*** RANK DATA FOR " + userRank.getRankName() + " ***");
            message.add("&b-&r Rank Identifier: " + userRank.getRankName());
            message.add("&b-&r Rank Tag: " + userRank.getFormat());
            message.add("&b-&r Rank Home Cap: " + userRank.getMaxHomes());
            message.add("&b-&r Rank Advancements: ");
            for (int i = 0; i < userRank.getAdvancementGate().size(); i++) {
                message.add("  &b-&r " + userRank.getAdvancementGate().get(i));
            }
            message.add("*** END RANK DATA ***");

            for (String message1: message) {
                player.sendMessage(message1);
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] strings) {
        ArrayList<String> args = new ArrayList<>();
        if (strings.length == 2) {
            for (int i = 0; i < RankStorage.findAllRanks().size(); i++) {
                args.add(RankStorage.findAllRanks().get(i).getRankName());
            }
        }
        return args;
    }
}
