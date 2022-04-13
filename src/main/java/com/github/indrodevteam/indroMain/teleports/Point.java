package com.github.indrodevteam.indroMain.teleports;

import com.github.indrodevteam.indroMain.dataUtils.LanguageTags;
import com.github.indrodevteam.indroMain.ranks.UserRanks;
import com.github.indrodevteam.indroMain.IndroMain;
import com.github.indrodevteam.indroMain.ranks.RankConfigTags;
import lombok.Data;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class Point {
    // identification for homes
    private String pointName;
    private String owner;
    private String pointType;

    // location for home
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;
    private String worldName;

    public Point(String pointName, String owner, String pointType, Location location) {
        this.pointName = pointName;
        this.owner = owner;
        this.pointType = pointType;

        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
        this.worldName = location.getWorld().getName();
    }

    public Location getLocation() {
        return new Location(Bukkit.getServer().getWorld(worldName), x, y, z, pitch, yaw);
    }

    // warp sets

    public void warp(Player player) {
        if (!IndroMain.warping.containsKey(player.getUniqueId())) {
            int delayWarpSeconds = (int) Double.parseDouble(UserRanks.getRank(player).getConfigTag(RankConfigTags.TIME_TO_WARP).toString());

            int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(IndroMain.getInstance(), new Runnable() {
                double var = 0;
                Location loc, first, second;

                @Override
                public void run() {
                    var += Math.PI / 16;

                    loc = player.getLocation();
                    first = loc.clone().add(Math.cos(var), Math.sin(var) + 1, Math.sin(var));
                    second = loc.clone().add(Math.cos(var + Math.PI), Math.sin(var) + 1, Math.sin(var + Math.PI));

                    player.getWorld().spawnParticle(Particle.TOTEM, first, 0);
                    player.getWorld().spawnParticle(Particle.TOTEM, second, 0);
                }
            }, 0, 1);
            Location location = new Location(Bukkit.getServer().getWorld(getWorldName()), getX(), getY(), getZ(), getYaw(), getPitch());
            AtomicInteger counter = new AtomicInteger(delayWarpSeconds);
            int warpID = Bukkit.getScheduler().scheduleSyncRepeatingTask(IndroMain.getInstance(), () -> {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.AQUA + "Warping in " + counter + "..."));
                player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 0.75F, 2.0F);
                if (counter.get() == -1) {
                    // allow warping
                    Bukkit.getScheduler().cancelTask(IndroMain.warping.get(player.getUniqueId()));
                    IndroMain.warping.remove(player.getUniqueId());
                    Bukkit.getScheduler().cancelTask(id);
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));

                    // warp players
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 0);
                    location.getChunk().load();
                    player.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 0);
                    player.teleport(location);
                    player.sendMessage(LanguageTags.JUMP_SUCCESS.get());
                }
                counter.getAndDecrement();
            }, 0, 20);
            IndroMain.warping.put(player.getUniqueId(), warpID);
            return;
        }
        player.sendMessage(LanguageTags.PLUGIN_TITLE.get() + "You are currently warping to another spot!");
    }
}
