package io.github.indroDevTeam.indroMain.dataUtils;

import io.github.indroDevTeam.indroMain.teleports.Point;
import io.github.indroDevTeam.indroMain.teleports.PointType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PointUtils {
    // CRUD: Create, Read, Update, Delete

    ArrayList<Point> points = new ArrayList<>();

    public Point createPoint(PointType pointType, String homeName, Player owner, Location location, Player... extraDetail) {
        String oof = "4.3213, 7.14423, 12.1243";

        String[] oofed = oof.split(", ");
        for (String s : oofed) {
            float coord = Float.parseFloat(s);

        }
    }
}
