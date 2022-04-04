package io.github.indroDevTeam.indroMain.commands.rank;

import io.github.indroDevTeam.indroMain.ranks.Rank;
import io.github.indroDevTeam.indroMain.ranks.RankConfigTags;
import io.github.indroDevTeam.indroMain.ranks.RankStorage;
import io.github.indroDevTeam.indroMain.ranks.RankUtils;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
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
        ArrayList<Rank> rankList = RankStorage.findAllRanks();
        if (commandSender instanceof Player player) {
            player.sendMessage("***** RANK LIST *****");
            for (Rank rankListOption: rankList) {
                ArrayList<String> rankTags = rankListOption.getNextRanks();
                if (rankTags == null) {rankTags = new ArrayList<>();}
                ArrayList<String> tagsToRemove = new ArrayList<>();

                for (int i = 0; i < rankTags.size(); i++) {
                    Rank e = RankStorage.readRank(rankListOption.getNextRanks().get(i));
                    if (e != null && (boolean) e.getConfigTag(RankConfigTags.SECRET)) {
                        tagsToRemove.add(rankTags.get(i));
                    }
                }
                for (String tags: tagsToRemove) {
                    rankTags.remove(tags);
                }
                String nextRanks;
                if (rankTags.isEmpty()) {
                    nextRanks = "nothing";
                } else {
                    nextRanks = rankTags.toString();
                }
                player.sendMessage("| Rank: " + rankListOption.getRankTag() +
                        " - Promotes to: " + nextRanks);
            }
            player.sendMessage("***** END RANK LIST *****");
        } else {
            commandSender.sendMessage("***** RANK LIST *****");
            for (Rank rankListOption: rankList) {
                ArrayList<String> rankTags = rankListOption.getNextRanks();
                if (rankTags == null) {rankTags = new ArrayList<>();}
                ArrayList<String> tagsToRemove = new ArrayList<>();

                for (int i = 0; i < rankTags.size(); i++) {
                    Rank e = RankStorage.readRank(rankListOption.getNextRanks().get(i));
                    if ((e != null) && (boolean) e.getRankConfig().get(RankConfigTags.SECRET)) {
                        tagsToRemove.add(rankTags.get(i));
                    }
                }
                for (String tags: tagsToRemove) {
                    rankTags.remove(tags);
                }
                commandSender.sendMessage("| Rank: " + rankListOption.getRankTag() +
                        " - Promotes to: " + rankTags);
            }
            commandSender.sendMessage("*** END RANK LIST ***");
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] strings) {
        return null;
    }
}
