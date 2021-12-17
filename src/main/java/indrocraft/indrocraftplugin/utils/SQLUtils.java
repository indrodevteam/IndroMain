package indrocraft.indrocraftplugin.utils;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUtils {

    //private final Main main = Main.getPlugin(Main.class);
    //private final SQLite sqLite = main.sqLite;

    private final Connection connection;

    public SQLUtils(Connection conn) {
        connection = conn;
    }

    //functions for editing database structure:

    /**
     * @param name     What name do you want to u se for the table
     * @param idColumn This is the unique ID column generally 'NAME'
     */
    public void createTable(String name, String idColumn) {
        try {
            PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + name +
                    " (" + idColumn + " VARCHAR(100),PRIMARY KEY (" + idColumn + "))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param id        This is the name of the column
     * @param dataType  What data type do u want to use
     * @param tableName The name of the table you want to put the column into
     */
    public void createColumn(String id, String dataType, String tableName) {
        try {
            PreparedStatement ps = connection.prepareStatement("ALTER TABLE " + tableName + " ADD IF NOT EXISTS "
                    + id + " " + dataType);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param idColumn  What is the ID column used in this table generally "NAME"
     * @param idEquals  What should the id of this row be? What is the name?
     * @param tableName What table dp you want to inset into?
     */
    public void createRow(String idColumn, String idEquals, String tableName) {
        if (!rowExists(idColumn, idEquals, tableName)) {
            try {
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + tableName);
                ResultSet results = ps.executeQuery();
                results.next();
                PreparedStatement ps2 = connection.prepareStatement("INSERT IGNORE INTO " +
                        tableName + " (" + idColumn + ") VALUE (?)");
                ps2.setString(1, idEquals);
                ps2.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param idColumn  What column in this table is unique generally the "NAME" column
     * @param test      What do u want to test the idColumn for
     * @param tableName In what table
     * @return Returns true if it exists and false if it does not
     */
    public boolean rowExists(String idColumn, String test, String tableName) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " +
                    tableName + " WHERE " + idColumn + "=?");
            ps.setString(1, test);

            ResultSet results = ps.executeQuery();
            if (results.next()) {
                //row is found
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param column    What is the name of the column you want to alter
     * @param dataType  What data type do u want to set it to
     * @param tableName In what table?
     */
    public void setDataType(String column, String dataType, String tableName) {
        try {
            PreparedStatement ps = connection.prepareStatement("ALTER TABLE " + tableName +
                    " MODIFY " + column + " " + dataType);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remove(String idColumn, String idEquals, String tableName) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM " + tableName +
                    " WHERE " + idColumn + "=?");
            ps.setString(1, idEquals);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //functions for writing data:

    /**
     * @param value     Data to be saved in form of a string, number will be converted depending on dataType
     * @param idColumn  identifier column to check
     * @param id        String that the id column is checked against
     * @param column    column to insert data
     * @param tableName table to insert data
     */
    public void setData(String value, String idColumn, String id, String column, String tableName) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE " + tableName + " SET " + column +
                    "=? WHERE " + idColumn + "=?");
            if (isNum("int", value)) {
                int valNum = Integer.parseInt(value);
                ps.setInt(1, valNum);
                Bukkit.getLogger().warning("int");
            } else if (isNum("float", value)) {
                float valNum = Float.parseFloat(value);
                ps.setFloat(1, valNum);
                Bukkit.getLogger().warning("float");
            } else if (isNum("double", value)) {
                double valNum = Double.parseDouble(value);
                ps.setDouble(1, valNum);
                Bukkit.getLogger().warning("double");
            } else {
                ps.setString(1, value);
            }
            ps.setString(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //reading data:

    /**
     * @param column    What column is the desired cell in
     * @param idColumn  What is the id column used for this table
     * @param idEquals  What id are you looking for?
     * @param tableName What is the name of the table
     * @return returns the number value of the specified cell
     */
    public int getInt(String column, String idColumn, String idEquals, String tableName) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT " + column + " FROM " + tableName +
                    " WHERE " + idColumn + "=?");
            ps.setString(1, idEquals);
            ResultSet rs = ps.executeQuery();
            int info = 0;
            if (rs.next()) {
                info = rs.getInt(column);
                return info;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * @param column    What column is the desired cell in
     * @param idColumn  What is the id column used for this table
     * @param idEquals  What id are you looking for?
     * @param tableName What is the name of the table
     * @return returns the string value of the specified cell
     */
    public String getString(String column, String idColumn, String idEquals, String tableName) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT " + column + " FROM " + tableName +
                    " WHERE " + idColumn + "=?");
            ps.setString(1, idEquals);
            ResultSet rs = ps.executeQuery();
            String info = "";
            if (rs.next()) {
                info = rs.getString(column);
                return info;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param column    What column is the desired cell in
     * @param idColumn  What is the id column used for this table
     * @param idEquals  What id are you looking for?
     * @param tableName What is the name of the table
     * @return returns the string value of the specified cell
     */
    public double getDouble(String column, String idColumn, String idEquals, String tableName) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT " + column + " FROM " + tableName +
                    " WHERE " + idColumn + "=?");
            ps.setString(1, idEquals);
            ResultSet rs = ps.executeQuery();
            double info = 0;
            if (rs.next()) {
                info = rs.getDouble(column);
                return info;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * @param column    What column is the desired cell in
     * @param idColumn  What is the id column used for this table
     * @param idEquals  What id are you looking for?
     * @param tableName What is the name of the table
     * @return returns the string value of the specified cell
     */
    public float getFloat(String column, String idColumn, String idEquals, String tableName) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT " + column + " FROM " + tableName +
                    " WHERE " + idColumn + "=?");
            ps.setString(1, idEquals);
            ResultSet rs = ps.executeQuery();
            float info = 0;
            if (rs.next()) {
                info = rs.getFloat(column);
                return info;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //exta utils:
    public boolean isNum(String type, String num) {
        try {
            if (type.equalsIgnoreCase("int")) {
                int i = Integer.parseInt(num);
                if (num == String.valueOf(i)) {
                    return true;
                } else {
                    return false;
                }
            } else if (type.equalsIgnoreCase("float")) {
                float i = Float.parseFloat(num);
                if (num == String.valueOf(i)) {
                    return true;
                } else {
                    return false;
                }
            } else if (type.equalsIgnoreCase("double")) {
                double i = Double.parseDouble(num);
                if (num == String.valueOf(i)) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
