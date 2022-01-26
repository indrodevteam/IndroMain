package indrocraft.indrocraftplugin.discord.botManager;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Leaderboard extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        String message = event.getMessage().getContentRaw();
        String[] args = message.split(" ");
        String PREFIX = "?";
        String statistic = "";

        if (message.startsWith(PREFIX) && args.length >= 2) {
            for (int i = 1; i < args.length - 1; i++) {
                statistic = statistic + args[i];
                if (i != args.length - 1)
                    statistic = statistic + " ";
            }
            if (args[0].equalsIgnoreCase("?leaderboard") || args[0].equalsIgnoreCase("?lb")) {
                if ("help".equals(args[1].toLowerCase())) {//help message embed
                    EmbedBuilder eb3 = new EmbedBuilder();
                    eb3.setTitle(":person_facepalming: Have I taught you nothing?").setColor(Color.red).
                            setFooter("Now you can try again!").addField("Your options are:",
                                    "`deaths\ntime played\ndamage dealt\ndiamonds_mined`", false);
                    event.getChannel().sendMessageEmbeds(eb3.build()).queue();
                } else {
                    Statistic stat = Statistic.TOTAL_WORLD_TIME;
                    String unit = "s";
                    switch (statistic.toLowerCase()) {
                        case "deaths":
                        case "death":
                            stat = Statistic.DEATHS;
                            break;
                        case "time played":
                            unit = "h";
                            break;
                        case "damage dealt":
                            stat = Statistic.DAMAGE_DEALT;
                            break;
                        case "damage taken":
                            stat = Statistic.DAMAGE_TAKEN;
                            break;


                    }
                    try {
                        leaderBoard(stat, event, args[1], unit);
                    } catch (Exception e) {
                        event.getMessage().reply("What is this '" + args[1] + "' you speak of?? :thinking:").queue();
                    }
                }
            }
        }
    }

    private void leaderBoard(Statistic statistic, MessageReceivedEvent event, String args, String unit) {
        EmbedBuilder leaderBoard = new EmbedBuilder();
        //get all players deaths in a list
        List<Integer> stat = new ArrayList<>();
        List<String> names = new ArrayList<>();
        //get all players for iteration
        List<OfflinePlayer> players = new ArrayList<>();
        players.addAll(Arrays.asList(Bukkit.getOfflinePlayers()));
        players.addAll(Bukkit.getOnlinePlayers());

        //get lists of all the players names along with their stats
        for (OfflinePlayer player : players) {
            if (statistic == Statistic.TOTAL_WORLD_TIME) {
                float ticks = (player.getStatistic(Statistic.TOTAL_WORLD_TIME));
                float hours = ticks / 20 / 60 / 60;
                DecimalFormat df = new DecimalFormat("0");
                String x = df.format(hours);
                stat.add(Integer.parseInt(x));
            } else {
                stat.add(player.getStatistic(statistic));
            }
            names.add(player.getName());
        }

        //get top 10
        List<Integer> topScore = new ArrayList<>();
        List<String> topNames = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            int x = stat.indexOf(Collections.max(stat));
            topScore.add(stat.get(x));
            topNames.add(names.get(x));
            stat.remove(stat.get(x));
            names.remove(names.get(x));
        }

        //create embed
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            if (value.length() > 0)
                value.append("\n");
            if (i == 0)
                value.append(":first_place: ");
            if (i == 1)
                value.append(":second_place: ");
            if (i == 2)
                value.append(":third_place: ");
            if (i == 3)
                value.append(":four: ");
            if (i == 4)
                value.append(":five: ");
            if (i == 5)
                value.append(":six: ");
            if (i == 6)
                value.append(":seven: ");
            if (i == 7)
                value.append(":eight: ");
            if (i == 8)
                value.append(":nine: ");
            if (i == 9)
                value.append(":keycap_ten: ");

            value.append("**").append(topScore.get(i)).append(unit).append("** - ").append(topNames.get(i));
        }
        leaderBoard.setColor(Color.TRANSLUCENT)
                .addField("**" + args.substring(0, 1).toUpperCase() +
                                args.substring(1) + " Leader board:**", value.toString(),
                        true);
        event.getChannel().sendMessageEmbeds(leaderBoard.build()).queue();
    }
}
