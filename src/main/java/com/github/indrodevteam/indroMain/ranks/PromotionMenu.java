package com.github.indrodevteam.indroMain.ranks;

import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PromotionMenu extends Menu {

    public PromotionMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Promotion Menu";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public boolean cancelAllClicks() {
        return false;
    }

    @Override
    public void handleMenu(InventoryClickEvent clickEvent) throws MenuManagerNotSetupException, MenuManagerException {
        ItemStack clickedItem = clickEvent.getCurrentItem();

        if (clickedItem.getType().equals(Material.GREEN_BANNER)) {
            clickEvent.setCancelled(true);
            Player player = (Player) clickEvent.getWhoClicked();
            // grabbed item
            String rankTag = clickedItem.getItemMeta().getDisplayName();
            Rank nextRank = RankStorage.readRank(rankTag);
            if (nextRank == null || UserRanks.getRank(player).equals(nextRank)) {
                player.closeInventory();
                player.sendMessage("But you're already that rank?");
                return;
            }
            RankUtils.promoteUser(player, nextRank);
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F);
            player.sendMessage("You have chosen to be promoted to " + rankTag);
            player.closeInventory();
        }
        clickEvent.setCancelled(true);
    }

    @Override
    public void setMenuItems() {
        Rank rank = UserRanks.getRank(playerMenuUtility.getOwner());
        if (rank.getNextRanks() == null || rank.getNextRanks().isEmpty()) {
            playerMenuUtility.getOwner().sendMessage("You have reached the end of the road...");
            return;
        }
        for (int i = 0; i < rank.getNextRanks().size(); i++) {
            Rank newRank = RankStorage.readRank(rank.getNextRanks().get(i));

            // null check
            if (newRank == null) {continue;}

            ItemStack item;
            ItemMeta meta;
            List<String> lore = new ArrayList<>();
            if (RankUtils.getEntryCriteria(p.getPlayer(), newRank)) {
                item = new ItemStack(Material.GREEN_BANNER);
                meta = item.getItemMeta();
                lore.add(ChatColor.GREEN + "You qualify for promotion to this rank!");
            } else if ((boolean) newRank.getConfigTag(RankConfigTags.SECRET) && !RankUtils.getEntryCriteria(p.getPlayer(), newRank)) {
                continue; // skip rendering that rank
            } else {
                item = new ItemStack(Material.RED_BANNER);
                meta = item.getItemMeta();
                lore.add(ChatColor.RED + "You do not qualify for promotion to this rank!");
            }
            assert meta != null;
            meta.setLore(lore);
            meta.setDisplayName(rank.getNextRanks().get(i));
            item.setItemMeta(meta);

            inventory.setItem(i, item);
        }
    }
}
