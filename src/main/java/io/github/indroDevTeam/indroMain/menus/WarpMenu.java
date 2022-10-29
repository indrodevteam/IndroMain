package io.github.indrodevteam.indroMain.menus;

import java.util.ArrayList;
import java.util.LinkedList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import io.github.indrodevteam.indroMain.IndroMain;
import io.github.indrodevteam.indroMain.model.Point;
import io.github.indrodevteam.indroMain.model.Profile;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.MenuManager;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;

public class WarpMenu extends Menu {
    private Profile profile;

    public WarpMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        profile = IndroMain.getProfileAPI().findProfile(playerMenuUtility.getOwner().getUniqueId());
    }

    @Override
    public String getMenuName() {
        return "Warp Menu";
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
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getType().equals(Material.BLACK_STAINED_GLASS_PANE)) return;
        if (e.getCurrentItem().getType().equals(Material.BARRIER)) {
            MenuManager.openMenu(ProfileMenu.class, (Player) e.getWhoClicked());
            return;
        }

        String pointName = e.getCurrentItem().getItemMeta().getDisplayName();
        Point point = profile.getPoint(pointName);

        if (e.getCurrentItem().getType().equals(Material.ENDER_PEARL)) {
            if (point == null) return;

            if (e.getClick().equals(ClickType.LEFT)) {
                IndroMain.getProfileAPI().findProfile(e.getWhoClicked().getUniqueId()).warp(p, point);
                p.closeInventory();
                return;    
            } 
            
            if (e.getClick().equals(ClickType.MIDDLE)) {
                LinkedList<Point> points = (LinkedList<Point>) profile.getPoints();

                points.remove(point);
                Point newPoint = new Point(pointName, e.getWhoClicked().getLocation());
                points.add(newPoint);
                profile.setPoints(points);

                IndroMain.sendParsedMessage((Player) e.getWhoClicked(), ChatColor.AQUA + "Relocated " + pointName + " to your location!");
                reload();
                return;
            } 
            
            if (e.getClick().equals(ClickType.RIGHT)) {
                ArrayList<Point> points = (ArrayList<Point>) profile.getPoints();
                points.remove(point);
                profile.setPoints(points);

                p.sendMessage(ChatColor.AQUA + "Removed " + point.getName() + "!");
                reload();
                return;
            }
        }
    }

    @Override
    public void setMenuItems() {
        /*
         * 00 01 02 03 04 05 06 07 08
         * 09 10 11 12 13 14 15 16 17
         * 18 19 20 21 22 23 24 25 26
         * 27 28 29 30 31 32 33 34 35
         */
        for (int i = 0; i < profile.getWarpCap(); i++) {
            int j = i+10;
            if (j+1 % 7 == 0) {
                j += 3;
            }

            if (profile.getPoints().size() > i) {
                inventory.setItem(j, makeItem(Material.ENDER_PEARL, profile.getPoints().get(i).getName(),
                        "Left Click to Warp there",
                        "Middle Click to Reposition",
                        "Right Click to Delete"
                ));
            } else {
                inventory.setItem(j, makeItem(Material.WHITE_STAINED_GLASS_PANE,"EMPTY WARP SLOT",
                        "Left click to create a warp at that location!"
                ));
            }
        }
        inventory.setItem(0, makeItem(Material.BARRIER, ChatColor.RED + "Back to Main Menu"));

        setFillerGlass();
    }


}
