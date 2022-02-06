package indrocraft.indrocraftplugin.discord.botManager;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Leaderboard extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        String message = event.getMessage().getContentRaw();
        String[] args = message.split(" ");

        HashMap<Integer, String> top10 = new HashMap<>();

        //make sure that the command has been requested and enough arguments have been provided
        if (args[0].equalsIgnoreCase("?lb") || args[0].equalsIgnoreCase("?leaderboard")) {
            if (args.length >= 2) {
                event.getMessage().reply("test").queue();
                //get all players for iteration
                List<OfflinePlayer> players = new ArrayList<>();
                players.addAll(Arrays.asList(Bukkit.getOfflinePlayers()));
                players.addAll(Bukkit.getOnlinePlayers());

                //get all players name and scores in a list
                List<Integer> stat = new ArrayList<>();
                List<String> names = new ArrayList<>();
                for (OfflinePlayer player : players) {
                    stat.add(player.getStatistic(Statistic.DEATHS));
                    names.add(player.getName());
                }

                //get top 10
                for (int i = 0; i < 10; i++) {
                    int x = stat.indexOf(Collections.max(stat));
                    //top10.put(x + 1, new HashMap<>());
                    stat.remove(stat.get(x));
                    names.remove(names.get(x));
                }

                //create embed
                EmbedBuilder leaderboard = new EmbedBuilder();
                StringBuilder value = new StringBuilder();
                for (int i = 0; i < 10; i++) {
                    if (value.length() > 0)
                        value.append("\n");
                    switch (i) {
                        case 0: value.append(":first_place: ");
                        case 1: value.append(":second_place: ");
                        case 2: value.append(":third_place: ");
                        case 3: value.append(":four: ");
                        case 4: value.append(":five: ");
                        case 5: value.append(":six: ");
                        case 6: value.append(":seven: ");
                        case 7: value.append(":eight: ");
                        case 8: value.append(":nine: ");
                        case 9: value.append(":keycap_ten: ");
                    }

                    //value.append("**").append(top10.get(i).getName()).append("** - ").append(top10.get(i).getScore());
                }
                leaderboard.setColor(Color.getHSBColor(123, 43, 123)).addField("Leader board:", value.toString(), true);
                event.getChannel().sendMessageEmbeds(leaderboard.build()).queue();
                event.getMessage().reply("test").queue();
            }
        }
    }
}
