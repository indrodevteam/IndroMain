package io.github.indroDevTeam.indroMain.commands;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.menus.ProfileMenu;
import io.github.indroDevTeam.indroMain.model.Profile;
import io.github.indroDevTeam.indroMain.utils.ChatUtils;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.MenuManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class CommandProfile implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            ChatUtils.notPlayerError(sender);
            return true;
        }

        Profile profile;
        if (IndroMain.getDataManager().getProfile(player.getUniqueId()).isEmpty()) {
            IndroMain.getDataManager().createProfile(Profile.getNewProfile(player, "default"));
        }
        profile = IndroMain.getDataManager().getProfile(player.getUniqueId()).get();

        /* Run checks for validity */
        if (args.length > 0) {
            ChatUtils.syntaxError(sender);
            return false;
        }

        try {
            MenuManager.openMenu(ProfileMenu.class, player);
        } catch (MenuManagerException | MenuManagerNotSetupException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
