package io.github.indroDevTeam.indroMain.teleports;

import io.github.indroDevTeam.indroMain.IndroMain;
import io.github.indroDevTeam.indroMain.dataUtils.LanguageTags;
import lombok.Data;
import org.bukkit.*;
import org.bukkit.entity.Player;

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
        int delayWarpSeconds;
        switch (PointType.valueOf(pointType)) {
            case PUBLIC_WARP -> delayWarpSeconds = 5;
            case PRIVATE_HOME -> delayWarpSeconds = 10;
            default -> delayWarpSeconds = 3;
        }

        Location location = new Location(Bukkit.getServer().getWorld(getWorldName()), getX(), getY(), getZ(), getPitch(), getYaw());
        
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
            player.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 0);
            player.teleport(location);
            player.sendMessage(LanguageTags.JUMP_SUCCESS.get());
        }, 20L * delayWarpSeconds);
    }


}
