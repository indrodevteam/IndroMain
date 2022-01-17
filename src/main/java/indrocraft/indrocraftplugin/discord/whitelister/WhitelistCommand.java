package indrocraft.indrocraftplugin.discord.whitelister;

import indrocraft.indrocraftplugin.Main;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import org.bukkit.Bukkit;

import java.util.Arrays;

public class WhitelistCommand extends ListenerAdapter {

    private final String PREFIX = "?";
    private final Main main = Main.getPlugin(Main.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        RestAction<Void> typing = event.getMessage().getChannel().sendTyping();
        String[] args = message.toLowerCase().split(" ");

        if (message.startsWith(PREFIX)) {
            switch (args[0]) {
                case "?channel":
                    //chooses the whitelisting channel
                    break;
                case "?list":
                    //lists everyone on the whitelist
                    String s = Arrays.toString(Bukkit.getWhitelistedPlayers().toArray());
                    TextChannel textChannel = main.bot.getJda().getTextChannelById("871625773910482974");
                    textChannel.sendMessage(s).queue();
                    break;
                case "?whitelist":
                    //handles whitelisting
                    break;
            }
        }
    }
}
