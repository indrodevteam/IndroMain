package io.github.indroDevTeam.indroMain;

import io.github.indroDevTeam.indroMain.data.Point;
import io.github.indroDevTeam.indroMain.data.Profile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PointManager {


    public static void warp(Player player, Profile profile, Point point) {
        int delayWarpSeconds = profile.getWarpDelay();

        Location location = point.getLocation();
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

        Bukkit.getScheduler().runTaskLater(IndroMain.getInstance(), () -> {
            Bukkit.getScheduler().cancelTask(id);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 0);
            location.getChunk().load();
            player.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 0);
            player.teleport(location);
            IndroMain.sendParsedMessage(player, "Teleport deployed!");
        }, 20L * delayWarpSeconds);
    }

}
