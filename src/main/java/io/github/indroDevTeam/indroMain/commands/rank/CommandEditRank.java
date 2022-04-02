package io.github.indroDevTeam.indroMain.commands.rank;

import io.github.indroDevTeam.indroMain.dataUtils.LanguageTags;
import io.github.indroDevTeam.indroMain.ranks.Rank;
import io.github.indroDevTeam.indroMain.ranks.RankStorage;
import io.github.indroDevTeam.indroMain.ranks.RankUtils;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommandEditRank extends SubCommand {
    /*
    What this does:
    - gets the name of the rank to edit
    - gets the thing to intercept
    -
     */
    @Override
    public String getName() {
        return "edit";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "allows admins to change the ranks";
    }

    @Override
    public String getSyntax() {
        return "/rank edit <rankToModify> <rankDataModified> <newModifiedValue>";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        sender.sendMessage(LanguageTags.ERROR_PERMISSION.get());
        String rankToModify, rankDataModified, newModifiedValue;
        if (args.length == 4) {
            rankToModify = args[1];
            rankDataModified = args[2];
            newModifiedValue = args[3];

            Rank rank = RankStorage.readRank(rankToModify);
            if (rank == null) {
                sender.sendMessage();
                return;
            }

            switch (rankValues.valueOf(rankDataModified)) {
                case RANK_ID -> rank.setRankName(newModifiedValue);
                case RANK_TAG -> rank.setFormat(rankDataModified);
                case RANK_MAX_HOMES -> rank.setMaxHomes(Integer.parseInt(newModifiedValue));
                default -> throw new IllegalStateException("Unexpected value: " + rankValues.valueOf(rankDataModified));
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        ArrayList<String> arguments = new ArrayList<>();
        if (args.length == 2) {
            for (int i = 0; i < RankStorage.findAllRanks().size(); i++) {
                arguments.add(RankStorage.findAllRanks().get(i).getRankName());
            }
        }
        if (args.length == 3) {
            arguments.add(rankValues.RANK_ID.toString());
            arguments.add(rankValues.RANK_TAG.toString());
            arguments.add(rankValues.RANK_MAX_HOMES.toString());
        }
        return arguments;
    }

    private enum rankValues {
        RANK_ID,
        RANK_TAG,
        //RANK_ADVANCEMENT_PROMOTION,
        RANK_MAX_HOMES
    }
}
