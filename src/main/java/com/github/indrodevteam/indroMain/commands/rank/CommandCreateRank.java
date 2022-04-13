package com.github.indrodevteam.indroMain.commands.rank;

import com.github.indrodevteam.indroMain.ranks.Rank;
import com.github.indrodevteam.indroMain.ranks.RankStorage;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CommandCreateRank extends SubCommand {
    @Override
    public String getName() {
        return "create";
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
            if (args.length >= 5 && player.isOp()) {
                String rankID = args[1];
                String format = ColorTranslator.translateColorCodes(args[2]);
                int maxHomes = Integer.parseInt(args[3]);
                ArrayList<String> nextRank = new ArrayList<>();
                nextRank.add(args[4]);
                ArrayList<String> advancementGate = new ArrayList<>(Arrays.asList(args).subList(4, args.length));

                RankStorage.createRank(rankID, format, nextRank, maxHomes, null, advancementGate, null);
                player.sendMessage("Added a rank!");
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] strings) {
        ArrayList<String> arguments = new ArrayList<>();
        if (player.isOp()) {
            switch (strings.length) {
                case 1, 2, 3 -> {}
                case 4 -> {
                    ArrayList<Rank> rankList = RankStorage.findAllRanks();
                    for (Rank rank : rankList) {
                        arguments.add(rank.getRankTag());
                    }
                }
                default -> {
                    Iterator<Advancement> it = Bukkit.getServer().advancementIterator();
                    // gets all 'registered' advancements on the server.
                    while (it.hasNext()) {
                        // loops through these.
                        Advancement a = it.next();
                        arguments.add(a.getKey().toString());
                    }
                }
            }
        }
        return arguments;
    }
}
