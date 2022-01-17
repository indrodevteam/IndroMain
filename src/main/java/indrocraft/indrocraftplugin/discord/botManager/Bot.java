package indrocraft.indrocraftplugin.discord.botManager;

import indrocraft.indrocraftplugin.discord.whitelister.WhitelistCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.Bukkit;

import javax.security.auth.login.LoginException;

public class Bot {

    private JDA jda;

    public Bot(String botToken) {
        try {
            jda = JDABuilder.createDefault(botToken)
                    .setActivity(Activity.competing("discord bot rap battle!"))
                    .setStatus(OnlineStatus.ONLINE)
                    .addEventListeners(new WhitelistCommand())
                    .build();
        } catch (LoginException e) {
            Bukkit.getLogger().severe("Bot was unable to login!");
        }
    }

    public JDA getJda() {
        return jda;
    }
}
