package io.github.indroDevTeam.indroMain.dataUtils;

import io.github.indroDevTeam.indroMain.teleports.Point;
import io.github.indroDevTeam.indroMain.teleports.PointType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PointUtils {
    // CRUD: Create, Read, Update, Delete

    private static ArrayList<Point> points = new ArrayList<>();

    public Point createPoint(PointType pointType, String homeName, String ownerUUID, Location location, Player... extraDetail) {
        Point point = new Point(pointType, homeName, ownerUUID, location);
        points.add(poj)
    }

    public Point findPoint(String ownerUUID, String homeName) {

    }
}
