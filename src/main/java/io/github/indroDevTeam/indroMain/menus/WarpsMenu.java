package io.github.indroDevTeam.indroMain.menus;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.model.Point;
import io.github.indroDevTeam.indroMain.model.Profile;
import io.github.indroDevTeam.indroMain.utils.ChatUtils;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class WarpsMenu extends Menu {
    private Profile profile;
    private final List<Point> points;
    public WarpsMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        if (IndroMain.getDataManager().getProfile(playerMenuUtility.getOwner().getUniqueId()).isPresent()) {
            profile = IndroMain.getDataManager().getProfile(playerMenuUtility.getOwner().getUniqueId()).get();
        }
        points = IndroMain.getDataManager().getPointByOwner(playerMenuUtility.getOwner().getUniqueId());
    }

    @Override
    public String getMenuName() {
        return ChatColor.BLUE + "Warps Menu";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {
        if (e.getSlot() == 0) {
            back();
            return;
        }
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getItemMeta() == null) return;

        ItemMeta meta = e.getCurrentItem().getItemMeta();
        String name = meta.getDisplayName();

        p.closeInventory();

        if (IndroMain.getDataManager().getPoint(p.getUniqueId(), name).isEmpty()) {
            ChatUtils.sendFailure(p, "This point does not exist!");
            return;
        }
        Point point = IndroMain.getDataManager().getPoint(p.getUniqueId(), name).get();
        if (point.getDistance(p) >= profile.getRank().getMaxDistance()) {
            ChatUtils.sendFailure(p, "You're too far away to teleport there!");
            return;
        }

        if (!profile.getRank().isCrossWorldPermitted() || !p.getLocation().getWorld().getName().equals(point.getLocation().getWorld().getName())) {
            ChatUtils.sendFailure(p, "This point is outside your dimension...");
            return;
        }

        if (IndroMain.getCooldowns().checkTeleportStatus(p)) {
            ChatUtils.sendFailure(p, "You're already teleporting to somewhere else!");
            return;
        }

        // teleport is cleared if it reaches here
        profile.warp(p, point);

        IndroMain.getDataManager().updateProfile(p.getUniqueId(), profile);
    }

    @Override
    public void setMenuItems() {


        int count = 10;
        int[] tinted = {17, 18, 26, 27, 35, 36};

        inventory.setItem(0, makeItem(
                Material.BARRIER,
                ChatColor.RED + "BACK"
        ));

        for (int i = 0; i < profile.getRank().getWarpCap(); i++) {
            if (points.size() > i) {
                inventory.setItem(count, makeItem(
                        Material.RED_BED,
                        points.get(i).getName(),
                        ChatColor.DARK_BLUE + "Left click to teleport here."
                ));
            } else {
                inventory.setItem(count, makeItem(
                        Material.WHITE_STAINED_GLASS_PANE,
                        "EMPTY SLOT"
                ));
            }

            count++;
            while (Objects.equals(tinted, count)) {
                count++;
            }
        }
        setFillerGlass(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
    }
}
