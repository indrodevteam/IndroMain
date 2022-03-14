package io.github.indroDevTeam.indroMain;

import io.github.indroDevTeam.indroMain.dataUtils.LanguageLoader;
import io.github.indroDevTeam.indroMain.events.EventOnRankUp;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.List;

public class Discord implements Listener {
    private JDA jda;

    public void init(String token) throws LoginException, InterruptedException {
        JDABuilder builder = JDABuilder.createDefault(token);

        // Set activity (like "playing Something")
        builder.setActivity(Activity.watching("your ranks."));

        jda = builder.build();
        jda.awaitReady();
    }

    @EventHandler
    public void onPlayerRankUp(EventOnRankUp event) {
        Integer discordRoleID = event.getNewRank().getDiscordID();
        if (discordRoleID != null) {
            Role role = jda.getRoleById(discordRoleID);
            if (role == null) {
                Bukkit.getServer().getLogger().warning(new LanguageLoader().get("plugin-title") + "Error: role does not exist!");
                return;
            }
        }
    }

    // getters and setters

    public JDA getJda() {
        return jda;
    }
}
