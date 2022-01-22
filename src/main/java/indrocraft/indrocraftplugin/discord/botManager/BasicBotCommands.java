package indrocraft.indrocraftplugin.discord.botManager;

import indrocraft.indrocraftplugin.Main;
import indrocraft.indrocraftplugin.utils.ConfigUtils;
import indrocraft.indrocraftplugin.utils.RankUtils;
import indrocraft.indrocraftplugin.utils.SQLUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.UUID;

public class BasicBotCommands extends ListenerAdapter {

    private final Main main = Main.getPlugin(Main.class);
    private final FileConfiguration r = new ConfigUtils(main, "rank.yml").getConfig();
    private final ConfigUtils config = new ConfigUtils(main, "config.yml");
    private final RankUtils rankUtils = new RankUtils();
    private final SQLUtils sqlUtils = new SQLUtils(config.getConfig().getString("database.database"),
            config.getConfig().getString("database.host"),
            config.getConfig().getString("database.port"),
            config.getConfig().getString("database.user"),
            config.getConfig().getString("database.password"));

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        String message = event.getMessage().getContentRaw();
        String[] args = message.split(" ");
        String PREFIX = "?";

        if (args[0].startsWith(PREFIX) && !event.getAuthor().isBot()) {
            switch (args[0]) {
                case "?ping":
                    event.getChannel().sendTyping().queue();
                    event.getMessage().reply("Pong!").queue();
                    break;
                case "?user":
                    if (args.length == 1) {
                        event.getChannel().sendTyping().queue();
                        event.getMessage().reply("Silly goose, you have to put a Minecraft username after that one!").queue();
                    } else {
                        if (playerExist(args[1])) {
                            String uuid = getUUID(args[1]);
                            OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
                            //get rank
                            String rank = sqlUtils.getString("rank", "UUID", uuid, "players");
                            //get server play time
                            float ticks = (player.getStatistic(Statistic.TOTAL_WORLD_TIME));
                            float hours = ticks / 20 / 60 / 60;
                            int s = player.getStatistic(Statistic.DEATHS);
                            DecimalFormat df = new DecimalFormat("0.00");
                            //get next advancement
                            String advance = getNextAdvancement(rank);
                            //get diamonds mined
                            int dMined = player.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE);
                            int dMined2 = player.getStatistic(Statistic.MINE_BLOCK, Material.DEEPSLATE_DIAMOND_ORE);
                            //get embed colour
                            Color colour = getRankColour(rank);

                            //build embed
                            EmbedBuilder eb = new EmbedBuilder();
                            eb.setTitle(args[1] + "'s user data:");
                            eb.addField("**RANK:**", rank, true);
                            if (!advance.equals("null"))
                                eb.addField("**NEXT ADVANCEMENT:**", advance, false);
                            eb.addField("**PLAY TIME:**", df.format(hours) + "h", false);
                            eb.addField("**DEATHS:**", String.valueOf(s), false);
                            eb.addField("**DIAMONDS MINED:**", "Diamond ore: " + dMined
                                    + "\nDeepslate diamond ore: " + dMined2, false);
                            eb.setColor(colour);
                            event.getChannel().sendMessageEmbeds(eb.build()).queue();
                        } else {
                            event.getMessage().reply("I wont fall for this trickery! That player doesnt exist.").queue();
                        }
                    }
                    break;
                case "?players":
                    StringBuilder result = new StringBuilder();
                    EmbedBuilder eb = new EmbedBuilder();
                    int count = 0;
                    for (OfflinePlayer player : Bukkit.getWhitelistedPlayers()) {
                        if (count % 20 == 0 && count != 0) {
                            result.append(",");
                        }
                        if (result.length() > 0) {
                            result.append("\n");
                        }
                        result.append(player.getName());
                        count++;
                    }

                    String[] groups = result.toString().split(",");
                    String[] list = result.toString().split("\n");

                    //make embed:
                    //setup
                    eb.setTitle("Whitelisted players: " + list.length);
                    try {
                        eb.setFooter("Showing " + groups[Integer.parseInt(args[1]) - 1].split("\n").length + "/" + list.length);
                    } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                        eb.setFooter("Showing ?/" + list.length);
                    }
                    eb.setColor(Color.GREEN);
                    //content
                    if (args.length >= 2 && isNumber(args[1]) && Integer.parseInt(args[1]) - 1 <= groups.length)
                        eb.addField("Whitelisted", groups[Integer.parseInt(args[1]) - 1], true);
                    else if (message.equalsIgnoreCase("?players")) {
                        eb.addField("Whitelisted", groups[0], true);
                        eb.setFooter("Showing " + groups[0].split("\n").length + "/" + list.length);
                    } else
                        eb.addField("Whitelisted", "This page hasn't been written yet! look away!", true);
                    event.getChannel().sendMessageEmbeds(eb.build()).queue();
                    break;

                case "?online":
                    EmbedBuilder eb2 = new EmbedBuilder();
                    StringBuilder onlinePlayers = new StringBuilder();
                    int counter = 0;
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (onlinePlayers.length() > 0) {
                            onlinePlayers.append("\n");
                        }
                        onlinePlayers.append(player.getName());
                        counter++;
                    }

                    eb2.setTitle("Online Players: " + Bukkit.getOnlinePlayers().size());
                    if (counter == 0)
                        eb2.addField("Online", "No one wants to play rn :cry:", true);
                    else
                        eb2.addField("Online", onlinePlayers.toString(), true);
                    eb2.setColor(Color.green);
                    event.getChannel().sendMessageEmbeds(eb2.build()).queue();
                    break;
            }
        }
    }

    private boolean playerExist(String name) {
        return sqlUtils.rowExists("ign", name, "players");
    }

    private String getUUID(String name) {
        return sqlUtils.getString("UUID", "ign", name, "players");
    }

    private String getNextAdvancement(String currentRank) {
        try {
            String s = r.getString("ranks." + currentRank + ".details.nextAdvancement");
            Advancement a = rankUtils.getAdvancement("minecraft:" + s);
            return a.getKey().getKey();
        } catch (NullPointerException e) {
            return "null";
        }
    }

    private Color getRankColour(String currentRank) {
        String colour = r.getString("ranks." + currentRank + ".colours.primary");
        try {
            ChatColor chatColor = rankUtils.readColour(colour);
            return chatColorToColor(chatColor);
        } catch (NullPointerException e) {
            return Color.WHITE;
        }
    }

    private Color getNameColour(OfflinePlayer player) {
        String colour = sqlUtils.getString("nameColour", "UUID", player.getUniqueId().toString(), "players");
        return chatColorToColor(rankUtils.readColour(colour));
    }

    private Color chatColorToColor(ChatColor colour) {
        switch (colour) {
            case DARK_RED:
            case RED:
                return Color.RED;
            case GOLD: return Color.ORANGE;
            case YELLOW: return Color.YELLOW;
            case DARK_GREEN:
            case GREEN:
                return Color.GREEN;
            case AQUA:
            case DARK_AQUA:
                return Color.CYAN;
            case DARK_BLUE: return Color.blue;
            case BLUE: return Color.BLUE;
            case LIGHT_PURPLE:
            case DARK_PURPLE:
                return Color.MAGENTA;
            case WHITE: return Color.WHITE;
            case GRAY: return Color.GRAY;
            case DARK_GRAY: return Color.DARK_GRAY;
            case BLACK: return Color.BLACK;
        }
        return Color.white;
    }

    private boolean isNumber(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
