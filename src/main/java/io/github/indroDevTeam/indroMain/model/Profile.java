package io.github.indroDevTeam.indroMain.model;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.utils.ChatUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Setter
public class Profile {
    private UUID userId;
    private String rankName;
    private int level, currentXp, nextXp;
    private Rank rank;

    public Profile(UUID userId, Rank rank, int level, int currentXp, int nextXp) {
        this.userId = userId;
        this.level = level;
        this.currentXp = currentXp;
        this.nextXp = nextXp;

        this.rank = rank;
        this.rankName = rank.getName();
    }

    public static Profile getNewProfile(Player player, String rankName) {
        Rank rank = IndroMain.getDataManager().getRank(rankName).isPresent() ? IndroMain.getDataManager().getRank(rankName).get() : null;
        if (rank == null) throw new RuntimeException("This rank doesn't exist!");

        return new Profile(player.getUniqueId(), rank, 1, 0, 10);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Class-based Methods
    ///////////////////////////////////////////////////////////////////////////

    private void teleportXp(Player player) {
        currentXp++;
        if (currentXp >= nextXp) {
            currentXp -= nextXp;
            level++;
            ChatUtils.sendSuccess(player, "You've been promoted to Level " + level);
        }
    }

    public void warp(Player player, @NotNull Point point) {
        boolean teleportCooldown = IndroMain.getCooldowns().isPlayerTeleportable(this);
        boolean teleportActive = IndroMain.getCooldowns().checkTeleportStatus(player);
        int timeRemaining = IndroMain.getCooldowns().getRemainingTime(this);

        if (teleportActive) {
            IndroMain.sendParsedMessage(player, ChatColor.BLUE + "A teleport is already queued up, please wait for that to complete before teleporting again.");
            return;
        }

        if (teleportCooldown) {
            IndroMain.sendParsedMessage(player, ChatColor.BLUE + "You've recently warped, wait " + timeRemaining + " seconds!");
            return;
        }

        // conditions have been fulfilled for a teleport
        IndroMain.sendParsedMessage(player, ChatColor.BLUE + "Teleporting...");
        Location location = point.getLocation();
        IndroMain.getCooldowns().setTeleportStatus(player, true);

        int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(IndroMain.getInstance(), new Runnable() {
            private double angle = 360;

            @Override
            public void run() {
                for (int i = 0; i < 90; i++) {
                    double radius = 0.5;
                    double x = (radius * Math.sin(angle));
                    double z = (radius * Math.cos(angle));
                    angle -= 0.1;

                    player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, player.getLocation().add(x, 2.75, z), 0, 0, -0.5, 0);
                }
                player.playSound(player.getLocation(), Sound.ENTITY_FROG_TONGUE, 0.5f, 0f);
            }
        }, 0, 10);

        Bukkit.getScheduler().runTaskLater(IndroMain.getInstance(), () -> {
            Bukkit.getScheduler().cancelTask(id);
            if (!player.getType().isAlive()) {
                IndroMain.sendParsedMessage(player, ChatColor.YELLOW + "Teleport failed!");
                IndroMain.getCooldowns().setTeleportStatus(player, false);
                return;
            }

            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 0);
            location.getChunk().load();
            player.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 0);
            player.teleport(location);
            IndroMain.sendParsedMessage(player, ChatColor.BLUE + "Teleport deployed!");

            teleportXp(player);
            player.setNoDamageTicks(40);
            IndroMain.getCooldowns().setTeleportStatus(player, false);
            IndroMain.getCooldowns().addTeleportCooldown(this);
        }, 20L * rank.getWarpDelay());
    }
}
