package io.github.indrodevteam.indroMain.menus;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import io.github.indrodevteam.indroMain.IndroMain;
import io.github.indrodevteam.indroMain.data.Profile;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import net.md_5.bungee.api.ChatColor;

public class ProfileMenu extends Menu {
    private Profile profile;

    public ProfileMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        profile = IndroMain.getProfileAPI().findProfile(playerMenuUtility.getOwner().getUniqueId());
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public String getMenuName() {
        return ChatColor.BLUE + "Profile Menu";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent arg0) throws MenuManagerNotSetupException, MenuManagerException {

    }

    @Override
    public void setMenuItems() {

        ItemStack profileStats = makeItem(Material.CLOCK, ChatColor.ITALIC.toString() + ChatColor.AQUA + "Profile Stats", 
            ChatColor.BLUE + "Player Name: " + profile.getPlayerName(),
            ChatColor.BLUE + "Level: " + profile.getLevel(),
            ChatColor.BLUE + "XP: " + profile.getCurrentXp() + "/" + profile.getNextXp()
        );


        inventory.setItem(0, profileStats);
    }
}