package indrocraft.indrocraftplugin.discord.whitelister;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class Add {
    public static void ExecuteCommand(SlashCommandEvent event, String mc_user, Member target) {
        if (event.getName().equals("whitelist")) {
            event.reply("yes").queue();
        }
    }
}
