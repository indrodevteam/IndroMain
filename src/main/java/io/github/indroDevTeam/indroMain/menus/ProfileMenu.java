package io.github.indroDevTeam.indroMain.menus;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.model.Profile;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.MenuManager;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ProfileMenu extends Menu {
    private Profile profile;

    public ProfileMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        if (IndroMain.getDataManager().getProfile(playerMenuUtility.getOwner().getUniqueId()).isPresent()) {
            profile = IndroMain.getDataManager().getProfile(playerMenuUtility.getOwner().getUniqueId()).get();
        }
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
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {

    }

    @Override
    public void setMenuItems() {
        ItemStack profileStats = makeItem(Material.CLOCK, ChatColor.ITALIC.toString() + ChatColor.AQUA + "Profile Stats",
            ChatColor.BLUE + "Level: " + profile.getLevel(),
            ChatColor.BLUE + "XP: " + profile.getCurrentXp() + "/" + profile.getNextXp()
        );

        ItemStack warpAccessor = makeItem(Material.BLUE_BANNER, ChatColor.GOLD + "To Warp Menu");

        inventory.setItem(0, profileStats);
        inventory.setItem(8, warpAccessor);
    }
}