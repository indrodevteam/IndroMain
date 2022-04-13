package com.github.indroDevTeam.indroMain.commands.rank;

import com.github.indroDevTeam.indroMain.ranks.*;
import me.kodysimpson.simpapi.colors.ColorTranslator;
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
        return "/rank info (RankTag)";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof Player player) {
            Rank userRank = null;
            if (strings.length == 1) {
                userRank = UserRanks.getRank(player);
            } else if (strings.length == 2) {
                userRank = RankStorage.readRank(strings[1]);
            }

            if (userRank == null) {
                player.sendMessage("Rank does not exist!");
                return;
            }

            ArrayList<String> message = new ArrayList<>();
            message.add("*** RANK DATA FOR " + userRank.getRankTag() + " ***");
            message.add("&b-&r Rank Identifier: " + userRank.getRankTag());
            message.add("&b-&r Rank Tag: " + RankUtils.translate(userRank.getFormat().replace("%player_name%", "")));
            message.add("&b-&r Rank Home Cap: " + userRank.getMaxHomes());
            message.add("&b-&r Rank Advancements: ");
            if (userRank.getAdvancementGate() != null) {
                for (int i = 0; i < userRank.getAdvancementGate().size(); i++) {
                    message.add("  &b-&r " + userRank.getAdvancementGate().get(i));
                }
            } else {
                message.add("  &b-&r No advancement required.");
            }

            message.add("&b-&r Rank Details: ");
            for (RankConfigTags config: RankConfigTags.values()) {
                if (userRank.getRankConfig().containsKey(config.toString())) {
                    message.add("  &b-&r " + config + ": " + userRank.getConfigTag(config));
                    continue;
                }
                message.add("  &b-&r No advancement required.");
            }
            message.add("*** END RANK DATA ***");

            for (String message1: message) {
                player.sendMessage(ColorTranslator.translateColorCodes(message1));
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] strings) {
        ArrayList<String> args = new ArrayList<>();
        if (strings.length == 2) {
            for (int i = 0; i < RankStorage.findAllRanks().size(); i++) {
                args.add(RankStorage.findAllRanks().get(i).getRankTag());
            }
        }
        return args;
    }
}
