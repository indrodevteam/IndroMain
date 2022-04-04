package io.github.indroDevTeam.indroMain.commands.rank;

import io.github.indroDevTeam.indroMain.ranks.PromotionMenu;
import io.github.indroDevTeam.indroMain.ranks.Rank;
import io.github.indroDevTeam.indroMain.ranks.UserRanks;
import me.kodysimpson.simpapi.command.SubCommand;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.MenuManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import java.awt.*;
import java.awt.desktop.AboutEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommandRankPromote extends SubCommand {
    @Override
    public String getName() {
        return "promote";
    }

    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("p");
        aliases.add("up");
        return aliases;
    }

    @Override
    public String getDescription() {
        return "This command gives you the option to promote!";
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof Player player) {
            try {
                MenuManager.openMenu(PromotionMenu.class, player);
            } catch (MenuManagerException | MenuManagerNotSetupException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] strings) {
        return null;
    }
}
