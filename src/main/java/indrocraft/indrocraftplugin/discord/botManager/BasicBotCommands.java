package indrocraft.indrocraftplugin.discord.botManager;

import indrocraft.indrocraftplugin.Main;
import indrocraft.indrocraftplugin.utils.ConfigUtils;
import indrocraft.indrocraftplugin.utils.RankUtils;
import indrocraft.indrocraftplugin.utils.SQLUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.advancement.Advancement;
import org.bukkit.configuration.file.FileConfiguration;

import java.awt.*;
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
                    event.getChannel().sendMessage("pong!").queue();
                    break;
                case "?user":
                    if (args.length == 1) {
                        event.getChannel().sendTyping().queue();
                        event.getMessage().reply("Silly goose, you have to put a Minecraft username after that one!").queue();
                    } else {
                        if (playerExist(args[1])) {
                            String uuid = getUUID(args[1]);
                            OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
                            //add title
                            EmbedBuilder eb = new EmbedBuilder();
                            eb.setTitle(args[1] + "'s user data:");
                            //get rank
                            String rank = sqlUtils.getString("rank", "UUID", uuid, "players");
                            //get server play time
                            float ticks = (player.getStatistic(Statistic.TOTAL_WORLD_TIME));
                            float hours = ticks / 20 / 60 / 60;
                            int s = player.getStatistic(Statistic.DEATHS);
                            DecimalFormat df = new DecimalFormat("0.00");
                            //get next advancement
                            String advance = getNextAdvancement(rank);
                            //get embed colour
                            Color colour = getRankColour(rank);

                            eb.addField("**RANK:**", rank, true);
                            if (!advance.equals("null"))
                                eb.addField("**NEXT ADVANCEMENT:**", advance, false);
                            eb.addField("**PLAY TIME:**", df.format(hours) + "h", false);
                            eb.addField("**DEATHS:**", String.valueOf(s), false);
                            eb.setColor(colour);
                            event.getChannel().sendMessageEmbeds(eb.build()).queue();
                        } else {
                            event.getMessage().reply("I wont fall for this trickery! That player doesnt exist.").queue();
                        }
                    }
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

    private Color chatColorToColor(ChatColor colour) {
        switch (colour) {
            case DARK_RED: return Color.getHSBColor(12, 32, 123);
            case RED: return Color.RED;
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
}
