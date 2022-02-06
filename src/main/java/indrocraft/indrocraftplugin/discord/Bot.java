package indrocraft.indrocraftplugin.discord;

import indrocraft.indrocraftplugin.discord.botManager.BasicBotCommands;
import indrocraft.indrocraftplugin.discord.botManager.Leaderboard;
import indrocraft.indrocraftplugin.discord.whitelister.WhitelistCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.bukkit.Bukkit;

import javax.security.auth.login.LoginException;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;
import static net.dv8tion.jda.api.interactions.commands.OptionType.USER;

public class Bot extends ListenerAdapter {

    private JDA jda;
    public Bot(String botToken) {
        try {
            jda = JDABuilder.createDefault(botToken)
                    .setActivity(Activity.competing("discord bot rap battle!"))
                    .setStatus(OnlineStatus.ONLINE)
                    .addEventListeners(new WhitelistCommand())
                    .addEventListeners(new BasicBotCommands())
                    .build();
        } catch (LoginException e) {
            Bukkit.getLogger().severe("Bot was unable to login!");
        }

        CommandListUpdateAction commands = jda.updateCommands();
        commands.addCommands(
                new CommandData("whitelist", "adds a player to the whitelist")
                        .addSubcommands(
                                new SubcommandData("add", "adds uer to the whitelist")
                                        .addOption(STRING, "minecraft_username", "Minecraft name to add", true)
                                        .addOption(USER, "discord_user", "Discord user to link to", false),
                                new SubcommandData("remove", "Remove user from the whitelist")
                                        .addOption(STRING, "minecraft_username", "Minecraft username to remove", true),
                                new SubcommandData("clear", "Clear whitelists assigned to your account"),
                                new SubcommandData("whois", "Find the Discord name linked to a Minecraft name")
                                        .addOption(STRING, "minecraft_username", "Minecraft name to search", false)
                                        .addOption(USER, "discord_user", "Minecraft name to search", false)))
                .queue();
    }

    public JDA getJda() {
        return jda;
    }
}
