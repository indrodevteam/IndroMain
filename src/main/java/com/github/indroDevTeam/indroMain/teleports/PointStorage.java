package com.github.indroDevTeam.indroMain.teleports;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.github.indroDevTeam.indroMain.IndroMain;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PointStorage {
    private static ArrayList<Point> points = new ArrayList<>();

    /**
     * A pure instance of creating a point
     * @param pointType The type of point created
     * @param pointName A unique name of point creted
     * @param owner A unique identifier of the user creating the point
     * @param location The location where the point is being created
     */
    public static void createPoint(PointType pointType, String pointName, String owner, Location location) {
        Point point = new Point(pointName, owner, pointType.toString(), location);
        points.add(point);
    }

    /**
     * Locates a point using the point name as a comparison
     * @param pointName The name of the point used.
     * @return The point if it exists, null if it doesn't
     */
    public static Point findPoint(String pointName) {
        for (Point point: points) {
            if (point.getPointName().equals(pointName)) {
                return point;
            }
        }
        return null;
    }

    /**
     * Locates a point using the location as a comparison
     * @param location The name of the point used.
     * @return The point if it exists, null if it doesn't
     */
    public static Point findPoint(Location location) {
        for (Point point: points) {
            if (point.getLocation().equals(location)) {
                return point;
            }
        }
        return null;
    }

    /**
     * Locates a point using the owner, and the homeName as a comparison
     * @param homeName The name of the point used.
     * @param owner The unique identifier of the owner.
     * @return The point if it exists, null if it doesn't
     */
    public static Point findPoint(String owner, String homeName) {
        for (Point point: points) {
            if (point.getOwner().equals(owner) && point.getPointName().equals(homeName)) {
                return point;
            }
        }
        return null;
    }

    public static ArrayList<Point> findPointsWithOwner(String owner) {
        ArrayList<Point> pointsByOwner = new ArrayList<>();
        for (Point point: points) {
            if (point.getOwner().equals(owner)) {
                pointsByOwner.add(point);
            }
        }
        return pointsByOwner;
    }

    public static ArrayList<Point> findPointsWithOwner(UUID ownerUUID) {
        String ownerID = ownerUUID.toString();
        ArrayList<Point> pointArrayList = new ArrayList<>();
        for (Point point: points) {
            if (point.getOwner().equals(ownerID)) {
                pointArrayList.add(point);
            }
        }
        return pointArrayList;
    }


    public static boolean editPoint(@Nullable String owner, String pointName, Point newPoint) {
        ArrayList<Point> pointArrayList;
        if (owner != null) {
            pointArrayList = findPointsWithOwner(owner);
        } else {
            pointArrayList = points;
        }
        for (Point point: pointArrayList) {
            if (point.getPointName().equals(pointName)) {
                point.setPointName(newPoint.getPointName());
                point.setWorldName(newPoint.getWorldName());
                point.setPointType(newPoint.getPointType());
                point.setOwner(newPoint.getOwner());
                point.setX(newPoint.getX());
                point.setY(newPoint.getY());
                point.setZ(newPoint.getZ());
                point.setPitch(newPoint.getPitch());
                point.setYaw(newPoint.getYaw());
                point.setWorldName(newPoint.getWorldName());

                return true;
            }
        }
        return false;
    }

    public static void deletePoint(String homeName, String owner) {
        points.removeIf(point -> point.getOwner().equals(owner) && point.getPointName().equals(homeName));
    }

    public static List<Point> findAllPoints() {
        return points;
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
