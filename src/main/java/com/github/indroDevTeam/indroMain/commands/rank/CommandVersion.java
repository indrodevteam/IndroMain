package com.github.indroDevTeam.indroMain.commands.rank;

import com.github.indroDevTeam.indroMain.IndroMain;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandVersion extends SubCommand {
    @Override
    public String getName() {
        return "version";
    }

    @Override
    public List<String> getAliases() {
        List<String> alias = new ArrayList<>();
        alias.add("v");
        return alias;
    }

    @Override
    public String getDescription() {
        return "Get the plugins version";
    }

    @Override
    public String getSyntax() {
        return "/rank version";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "Your current version is v" +
                        IndroMain.getInstance().getConfig().getString("version"))
                );
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
