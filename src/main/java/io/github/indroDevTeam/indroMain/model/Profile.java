package io.github.indroDevTeam.indroMain.model;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.utils.ChatUtils;
import lombok.*;
import org.bukkit.*;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import javax.sql.rowset.serial.SerialArray;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class Profile implements ConfigurationSerializable {
    private UUID playerId;
    //private String rankId;
    private int level;
    private int currentXp;
    private int nextXp;

    private transient Rank rank;
    private transient LocalDateTime cooldownTime = null;
    private transient boolean teleportActive = false;

    public Profile(UUID playerId,
                   //Rank rank,
                   int level, int currentXp, int nextXp) {
        this.playerId = playerId;
        this.rank = new Rank("Test", "{TEST}", "[TEST]", 2, 10, 15, 100, true);
        //this.rankId = rank.getId();
        this.level = level;
        this.currentXp = currentXp;
        this.nextXp = nextXp;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Class-based Methods
    ///////////////////////////////////////////////////////////////////////////

    public void warp(Player player, @NotNull Home home) {
        if (teleportActive) {
            ChatUtils.sendFailure(player, ChatColor.BLUE + "A teleport is already queued up, please wait for that to complete before teleporting again.");
            return;
        }

        if (cooldownTime != null && LocalDateTime.now().isBefore(cooldownTime)) {
            ChatUtils.sendFailure(player, "You've recently warped, wait " + (cooldownTime.toEpochSecond(ZoneOffset.UTC) - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)) + " seconds!");
            return;
        }

        // conditions have been fulfilled for a teleport
        ChatUtils.sendInfo(player, "Teleporting...");
        Location location = home.getLocation();
        teleportActive = true;

        int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(IndroMain.getInstance(), new Runnable() {
            private double angle = 360;

            @Override
            public void run() {
                for (int i = 0; i < 90; i++) {
                    double radius = 0.5;
                    double y = (radius * Math.sin(angle));
                    double z = (radius * Math.cos(angle));
                    angle -= 0.1;

                    player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation().add(y, 2.5, z), 0, 0, -0.5, 0);
                }
            }
        }, 0, 10);

        Bukkit.getScheduler().runTaskLater(IndroMain.getInstance(), () -> {
            Bukkit.getScheduler().cancelTask(id);
            if (player.getType().isAlive()) {
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 0);
                location.getChunk().load();
                player.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 0);
                player.teleport(location);
                ChatUtils.sendSuccess(player, "Teleport deployed!");

                this.setCurrentXp(this.getCurrentXp() + 1);
                teleportActive = false;
                return;
            }
            
            ChatUtils.sendInfo(player, "Teleport failed!");
            teleportActive = false;
        }, 20L * rank.getWarpDelay());

        this.cooldownTime = LocalDateTime.now().plusSeconds(rank.getWarpCooldown());
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("playerId", playerId);
        map.put("level", level);
        map.put("currentXp", currentXp);
        map.put("nextXp", nextXp);

        return map;
    }

    public static Profile deserialize(Map<String, Object> map) {
        UUID playerId = null;
        String rankId = null;
        int level = 1;
        int currentXp = 0;
        int nextXp = 5;

        if (map.containsKey("playerId")) {
            playerId = ((UUID) map.get("playerId"));
        }

        if (map.containsKey("level")) {
            level = ((int) map.get("level"));
        }

        if (map.containsKey("currentXp")) {
            currentXp = ((int) map.get("currentXp"));
        }

        if (map.containsKey("nextXp")) {
            nextXp = ((int) map.get("nextXp"));
        }

        return new Profile(playerId, level, currentXp, nextXp);
    }
}
