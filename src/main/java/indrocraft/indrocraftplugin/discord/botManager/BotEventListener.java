package indrocraft.indrocraftplugin.discord.botManager;

import indrocraft.indrocraftplugin.Main;
import indrocraft.indrocraftplugin.events.ServerRankEvent;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BotEventListener implements Listener {

    private final Main main = Main.getPlugin(Main.class);

    @EventHandler
    public void onServerRankEvent(ServerRankEvent event) {
        TextChannel textChannel = main.bot.getJda().getTextChannelById("871625773910482974");
        textChannel.sendMessage(event.getPlayer().getName() + " just got to the rank: " + event.getRank()).queue();
    }

}
