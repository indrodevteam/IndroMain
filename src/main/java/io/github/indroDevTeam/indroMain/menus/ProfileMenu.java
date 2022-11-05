package io.github.indroDevTeam.indroMain.menus;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.model.DaoProfile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import io.github.indroDevTeam.indroMain.model.Profile;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.MenuManager;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import net.md_5.bungee.api.ChatColor;

import java.sql.SQLException;

public class ProfileMenu extends Menu {
    private Profile profile;

    public ProfileMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        try {
            if (DaoProfile.getInstance().find(playerMenuUtility.getOwner().getUniqueId()).isPresent()) {
                profile = DaoProfile.getInstance().find(playerMenuUtility.getOwner().getUniqueId()).get();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {
        //if (e.getCurrentItem().getType().equals(Material.BLUE_BANNER)) {
        //    MenuManager.openMenu(WarpMenu.class, (Player) e.getWhoClicked());
        //}
    }

    @Override
    public void setMenuItems() {
        ItemStack profileStats = makeItem(Material.CLOCK, ChatColor.ITALIC.toString() + ChatColor.AQUA + "Profile Stats", 
            ChatColor.BLUE + "Player Name: " + profile.getPlayerName(),
            ChatColor.BLUE + "Level: " + profile.getLevel(),
            ChatColor.BLUE + "XP: " + profile.getCurrentXp() + "/" + profile.getNextXp()
        );

        ItemStack warpAccessor = makeItem(Material.BLUE_BANNER, ChatColor.GOLD + "To Warp Menu");

        inventory.setItem(0, profileStats);
        inventory.setItem(12, warpAccessor);
    }
}