package indrocraft.indrocraftplugin.utils;

import indrocraft.indrocraftplugin.Main;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLUtils {
    /*
    todo:
        - create (table | row | column)
        - delete (table | row | column)
        - change data type
        - item exists
        - clear (table | row | column)
        - set (int | string | double | float)
        - get (int | string | double | float)
     */

    private Main plugin;
    public SQLUtils(Main plugin) {this.plugin = plugin;}

    /**
     * @param value Data to be saved in form of a string, number will be converted depending on dataType
     * @param idColumn identifier column to check
     * @param id String that the id column is checked against
     * @param column column to insert data
     * @param tableName table to insert data
     */
    public void setData(String value, String idColumn, String id, String column, String tableName) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE " + tableName + " SET " + column + "=? WHERE " + idColumn + "=?");
            if (isNum("int", value)) {
                int valNum = Integer.valueOf(value);
                ps.setInt(1, valNum);
                ps.setString(2, id);
            } else if (isNum("double", value)) {
                double valNum = Double.valueOf(value);
                ps.setDouble(1, valNum);
                ps.setString(2, id);
            } else if (isNum("float", value)) {
                float valNum = Float.valueOf(value);
                ps.setFloat(1, valNum);
                ps.setString(2, id);
            } else {
                return;
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param name What name do you want to u se for the table
     * @param idColumn This is the unique ID column generally 'NAME'
     */
    public void createTable(String name, String idColumn) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + name + " (" + idColumn + " VARCHAR(100),PRIMARY KEY (" + idColumn + "))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param id This is the name of the column
     * @param dataType What data type do u want to use
     * @param tableName The name of the table you want to put the column into
     */
    public void createColumn(String id, String dataType, String tableName) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("ALTER TABLE " + tableName + " ADD IF NOT EXISTS " + id + " " + dataType);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param idColumn What is the ID column used in this table generally "NAME"
     * @param idEquals What should the id of this row be? What is the name?
     * @param tableName What table dp you want to inset into?
     */
    public void createRow(String idColumn, String idEquals, String tableName) {
        if (!exists(idColumn, idEquals, tableName)) {
            try {
                PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM " + tableName);
                ResultSet results = ps.executeQuery();
                results.next();
                PreparedStatement ps2 = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO " + tableName + " (NAME) VALUE (?)");
                ps2.setString(1, idEquals);
                ps2.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Bukkit.getLogger().warning("this row already exists");
        }
    }

    public boolean exists(String idColumn, String test, String tableName) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM " + tableName + " WHERE " + idColumn + "=?");
            ps.setString(1, test);

            ResultSet results = ps.executeQuery();
            if (results.next()) {
                //player is found
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isNum(String type, String num) {
        try {
            if (type.equalsIgnoreCase("int")) {
                Integer.parseInt(num);
            } else if (type.equalsIgnoreCase("double")) {
                Bukkit.getLogger().warning("double");
                Double.parseDouble(num);
            } else if (type.equalsIgnoreCase("float")) {
                Bukkit.getLogger().warning("Float");
                Float.parseFloat(num);
            }
        } catch (Exception e) {
            if (type.equalsIgnoreCase("int")) {
                Bukkit.getLogger().warning("Not an Integer");
            } else if (type.equalsIgnoreCase("double")) {
                Bukkit.getLogger().warning("Not an Double");
            } else if (type.equalsIgnoreCase("float")) {
                Bukkit.getLogger().warning("Not an Float");
            }
            return false;
        }
        return true;
    }
}
