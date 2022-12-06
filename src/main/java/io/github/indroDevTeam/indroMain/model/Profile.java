package io.github.indroDevTeam.indroMain.model;

import io.github.indroDevTeam.indroMain.IndroMain;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Getter
@Setter
public class Profile {
    private UUID userId;
    private String rankId;
    private int level, currentXp, nextXp;
    
    private transient Rank rank;
    private transient LocalDateTime cooldownTime;
    private transient boolean teleportActive = false;

    public Profile(UUID userId, String rankId, int level, int currentXp, int nextXp) {
        this.userId = userId;
        this.rankId = rankId;
        this.level = level;
        this.currentXp = currentXp;
        this.nextXp = nextXp;

        //TODO: Modify this to take from ranks data
        this.rank = new Rank("Test", "{TEST}", "[TEST]", 2, 10, 15, 250, true);
    }

    public static Profile getNewProfile(Player player, String rankId) {
        return new Profile(player.getUniqueId(), rankId, 1, 0, 10);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Class-based Methods
    ///////////////////////////////////////////////////////////////////////////

    public void warp(Player player, @NotNull Point point) {
        if (teleportActive) {
            IndroMain.sendParsedMessage(player, ChatColor.BLUE + "A teleport is already queued up, please wait for that to complete before teleporting again.");
            return;
        }

        if (cooldownTime != null && LocalDateTime.now().isBefore(cooldownTime)) {
            IndroMain.sendParsedMessage(player, ChatColor.BLUE + "You've recently warped, wait " + (cooldownTime.toEpochSecond(ZoneOffset.UTC) - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)) + " seconds!");
            return;
        }

        // conditions have been fulfilled for a teleport
        IndroMain.sendParsedMessage(player, ChatColor.BLUE + "Teleporting...");
        Location location = point.getLocation();
        teleportActive = true;

        int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(IndroMain.getInstance(), new Runnable() {
            private double angle = 360;

            @Override
            public void run() {
                for (int i = 0; i < 90; i++) {
                    double radius = 0.5;
                    double x = (radius * Math.sin(angle));
                    double z = (radius * Math.cos(angle));
                    angle -= 0.1;

                    player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation().add(x, 2.5, z), 0, 0, -0.5, 0);
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
                IndroMain.sendParsedMessage(player, ChatColor.BLUE + "Teleport deployed!");

                this.setCurrentXp(this.getCurrentXp() + 1);
                teleportActive = false;
                return;
            }
            
            IndroMain.sendParsedMessage(player, ChatColor.YELLOW + "Teleport failed!"); 
            teleportActive = false;
        }, 20L * rank.getWarpDelay());

        this.cooldownTime = LocalDateTime.now().plusSeconds(rank.getWarpCooldown());
    }
}
