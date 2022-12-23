package io.github.indroDevTeam.indroMain.menus;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.model.Profile;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class StatsMenu extends Menu {
    private Profile profile;
    public StatsMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        if (IndroMain.getDataManager().getProfile(playerMenuUtility.getOwner().getUniqueId()).isPresent()) {
            profile = IndroMain.getDataManager().getProfile(playerMenuUtility.getOwner().getUniqueId()).get();
        }
    }

    @Override
    public String getMenuName() {
        return ChatColor.BLUE + "Stats Menu";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {
        if (e.getSlot() == 0) {
            back();
        }
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(0, makeItem(
                Material.BARRIER,
                ChatColor.RED + "BACK"
        ));

        inventory.setItem(10, makeItem(
                Material.WHITE_BANNER,
                ChatColor.BLUE + "Rank Name: " + profile.getRank().getName()
        ));

        inventory.setItem(11, makeItem(
                Material.WHITE_BANNER,
                ChatColor.BLUE + "Rank Display",
                "Chat Tag: " + profile.getRank().getChatTag(),
                "Tab Tag: " + profile.getRank().getTabTag()
        ));

        inventory.setItem(12, makeItem(
                Material.WHITE_BANNER,
                ChatColor.BLUE + "Teleport Stats",
                "Warp Cap: " + profile.getRank().getWarpCap() + " points",
                "Warp Delay: " + profile.getRank().getWarpDelay() + " secs",
                "Warp Cooldown: " + profile.getRank().getWarpCooldown() + " secs",
                "Max Distance: " + profile.getRank().getMaxDistance() + "m",
                "Teleport across world: " + (profile.getRank().isCrossWorldPermitted() ? ChatColor.GREEN + "Permitted" : ChatColor.RED + "Denied")
        ));
    }
}
