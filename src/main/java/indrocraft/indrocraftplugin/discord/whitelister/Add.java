package indrocraft.indrocraftplugin.discord.whitelister;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;

public class Add extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        Bukkit.getLogger().severe("poopie did slash" + event.getCommandString());
        if (event.getName().equals("whitelist")) {
            event.reply("yes").queue();
            Bukkit.getWhitelistedPlayers();
        }
    }

    //, String mc_user, Member target
}
