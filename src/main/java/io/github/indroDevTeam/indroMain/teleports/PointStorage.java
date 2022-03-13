package io.github.indroDevTeam.indroMain.teleports;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.indroDevTeam.indroMain.IndroMain;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class PointStorage {
    // CRUD: Create, Read, Update, Delete

    private static ArrayList<Point> points = new ArrayList<>();

    public static void createPoint(String pointType, String homeName, String ownerUUID, Location location) {
        Point point = new Point(pointType, homeName, ownerUUID, location);
        points.add(point);
    }

    public static Point findPoint(String owner, String homeName) {
        for (Point point : points) {
            if (point.getOwner().equalsIgnoreCase(owner) && point.getHomeName().equals(homeName)) {
                return point;
            }
        }
        return null;
    }

    public static Point updatePoint(String homeName, Point newPoint) {
        for (Point point : points) {
            if (point.getHomeName().equals(homeName)) {
                point.setPointType(newPoint.getPointType());
                point.setHomeName(newPoint.getHomeName());
                point.setOwner(newPoint.getOwner());

                // done with an edithome command
                point.setX(newPoint.getX());
                point.setY(newPoint.getY());
                point.setZ(newPoint.getZ());
                point.setWorldName(newPoint.getWorldName());
                point.setPitch(newPoint.getPitch());
                point.setYaw(newPoint.getYaw());

                return point;
            }
        }
        return null;
    }

    public static void deletePoint(String homeName) {
        points.removeIf(point -> point.getHomeName().equalsIgnoreCase(homeName));
    }

    public static ArrayList<Point> findAllPoints() {return points;}

    public static ArrayList<Point> findPointsWithOwner(String playerUUID) {
        ArrayList<Point> pointArrayList = new ArrayList<>();
        for (Point point : points) {
            if (point.getOwner().equals(playerUUID)) {
                pointArrayList.add(point);
            }
        }
        return pointArrayList;
    }

    public static void savePoints() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(IndroMain.getInstance().getDataFolder().getAbsolutePath() + File.separator + "points.json");
        if (!file.exists()) {
            file.getParentFile().mkdir();
            file.createNewFile();
        }

        Writer writer = new FileWriter(file, false);
        gson.toJson(points, writer);
        writer.flush();
        writer.close();
    }

    public static void loadPoints() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(IndroMain.getInstance().getDataFolder().getAbsolutePath() + File.separator + "points.json");
        if (!file.exists()) {
            savePoints();
        }
        Point[] model = gson.fromJson(new FileReader(file), Point[].class);
        points = new ArrayList<>(Arrays.asList(model));
    }
}
