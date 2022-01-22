package indrocraft.indrocraftplugin.utils;

import org.bukkit.Bukkit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLUtils {

    public Connection connection;

    private final Connection conn;
    private boolean conn() {
        return conn == null;
    }

    /*
     *  +-----------------------------------------------+
     *  |                 SQL Connector:                |
     *  |   This section connects to the database and   |
     *  |    sets the credentials for the connection    |
     *  +-----------------------------------------------+
     */

    public final String password;
    public final String host;
    public final String port;
    public final String database;
    public final String username;

    /**
     * @param database What is the name of the database you want to connect to?
     * @param host     Where is the database being hosted? If it's on this machine set this parameter to localhost.
     * @param port     What port is the server running on, the default port for MySQL is 3306.
     * @param username What user do you want to access the database as? Should be set to 'root' if possible.
     * @param password What password does that username use? If none just leave as an empty string "".
     */
    public SQLUtils(String database, String host, String port, String username, String password) {
        this.password = password;
        this.username = username;
        this.database = database;
        this.host = host;
        this.port = port;

        conn = getConnection();
        if (conn == null) {
            Bukkit.getLogger().severe("Connection unsuccessful!");
        }
    }

    public Connection getConnection() {
        connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://"
                            + host + ":" + port + "/" + database + "?useSSL=false"
                    , username, password);
        } catch (SQLException e) {
            printSQLException(e);
            return null;
        }/* catch (CJCommunicationsException e) {
            System.out.println();
            return null;
        }*/
        return connection;
    }

    public void closeConnection(Connection connArg) {
        System.out.println("Closing the Database...");
        try {
            connArg.close();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                //e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException)e).getSQLState() + "\n");
                System.err.println("Error Code: " + ((SQLException)e).getErrorCode() + "\n");
                System.err.println("Message: " + e.getMessage() + "\n");
                System.err.println("error at line: " + e.getStackTrace()[e.getStackTrace().length-1] + "\n");
                System.err.println("The database is not connected! please ensure that the login credentials are " +
                        "correct and the database is running!");

                /*Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }*/
            }
        }
    }

    /*
    *  +-----------------------------------------------+
    *  |                   SQL Utils:                  |
    *  |    methods for setting data, getting data     |
    *  | as well as some smaller methods like countRows|
    *  +-----------------------------------------------+
    */

    /**
     * @param value     Data to be saved in form of a string, number will be converted depending on dataType
     * @param idColumn  identifier column to check
     * @param id        String that the id column is checked against
     * @param column    column to insert data
     * @param tableName table to insert data
     */
    public void setData(String value, String idColumn, String id, String column, String tableName) {
        if (conn()) {return;}
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE " + tableName + " SET " + column + "=? WHERE "
                    + idColumn + "=?");
            if (isNum("int", value)) {
                int valNum = Integer.parseInt(value);
                ps.setInt(1, valNum);
                ps.setString(2, id);
            } else if (isNum("float", value)) {
                float valNum = Float.parseFloat(value);
                ps.setFloat(1, valNum);
                ps.setString(2, id);
            } else if (isNum("double", value)) {
                double valNum = Double.parseDouble(value);
                ps.setDouble(1, valNum);
                ps.setString(2, id);
            } else {
                ps.setString(1, value);
                ps.setString(2, id);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param column    What column is the desired cell in
     * @param idColumn  What is the id column used for this table
     * @param idEquals  What id are you looking for?
     * @param tableName What is the name of the table
     * @return returns the number value of the specified cell
     */
    public int getInt(String column, String idColumn, String idEquals, String tableName) {
        if (conn()) {return 0;}
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT " + column + " FROM " + tableName + " WHERE "
                    + idColumn + "=?");
            ps.setString(1, idEquals);
            ResultSet rs = ps.executeQuery();
            int info;
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
        if (conn()) {return "";}
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT `" + column + "` FROM " + tableName + " WHERE "
                    + idColumn + "=?");
            ps.setString(1, idEquals);
            ResultSet rs = ps.executeQuery();
            String info;
            if (rs.next()) {
                info = rs.getString(column);
                return info;
            }
        } catch (SQLSyntaxErrorException e) {
            try {
                PreparedStatement p = conn.prepareStatement("UPDATE " + tableName + " SET `" + column
                        + "`=' ' WHERE " + idColumn + "=" + idEquals);
                p.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
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
        if (conn()) {return 0;}
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT " + column + " FROM " + tableName + " WHERE "
                    + idColumn + "=?");
            ps.setString(1, idEquals);
            ResultSet rs = ps.executeQuery();
            double info;
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
        if (conn()) {return 0;}
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT " + column + " FROM " + tableName + " WHERE "
                    + idColumn + "=?");
            ps.setString(1, idEquals);
            ResultSet rs = ps.executeQuery();
            float info;
            if (rs.next()) {
                info = rs.getFloat(column);
                return info;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<String> getEntireColumn(String columnName, String tableName) {
        if (conn()) {return  new ArrayList<>();}
        try {
            List<String> data = new ArrayList<>();
            PreparedStatement ps = conn.prepareStatement("SELECT " + columnName + " FROM " + tableName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                data.add(rs.getString(columnName));
            }
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * @param name     What name do you want to u se for the table
     * @param idColumn This is the unique ID column generally 'NAME'
     */
    public void createTable(String name, String idColumn) {
        if (conn()) {return;}
        try {
            PreparedStatement ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS " + name + " (" + idColumn
                    + " VARCHAR(100),PRIMARY KEY (" + idColumn + "))");
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
        if (conn()) {return;}
        try {
            if (!columnExists(id, tableName)) {
                PreparedStatement ps = conn.prepareStatement("ALTER TABLE " + tableName + " ADD " + id + " "
                        + dataType + " NOT NULL");
                ps.executeUpdate();
            }
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
        if (conn()) {return;}
        if (!rowExists(idColumn, idEquals, tableName)) {
            try {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + tableName);
                ResultSet results = ps.executeQuery();
                results.next();
                PreparedStatement ps2 = conn.prepareStatement("INSERT IGNORE INTO " + tableName + " ("
                        + idColumn + ") VALUE (?)");
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
        if (conn()) {return false;}
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE " + idColumn
                    + "=?");
            ps.setString(1, test);

            ResultSet results = ps.executeQuery();
            //row is found
            return results.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param name      Name of the column you are checking for
     * @param tableName name of the target table
     * @return returns true if the column exists else returns false
     */
    public boolean columnExists(String name, String tableName) {
        if (conn()) {return false;}
        try {
            PreparedStatement ps = conn.prepareStatement("show columns from " + tableName +
                    " where field = ?");
            ps.setString(1, name);
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int countRows(String tableName) {
        if (conn()) {return 0;}
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT count(*) FROM " + tableName);
            ResultSet rs = ps.executeQuery();
            int info;
            if (rs.next()) {
                info = rs.getInt("count(*)");
                return info;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * @param column    What is the name of the column you want to alter
     * @param dataType  What data type do u want to set it to
     * @param tableName In what table?
     */
    public void setDataType(String column, String dataType, String tableName) {
        if (conn()) {return;}
        try {
            PreparedStatement ps = conn.prepareStatement("ALTER TABLE " + tableName + " MODIFY " + column + " "
                    + dataType);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remove(String idColumn, String idEquals, String tableName) {
        if (conn()) {return;}
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM " + tableName + " WHERE " + idColumn + "=?");
            ps.setString(1, idEquals);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isNum(String type, String num) {
        try {
            if (type.equalsIgnoreCase("int")) {
                int i = Integer.parseInt(num);
                return num.equals(String.valueOf(i));
            } else if (type.equalsIgnoreCase("float")) {
                float i = Float.parseFloat(num);
                return num.equals(String.valueOf(i));
            } else if (type.equalsIgnoreCase("double")) {
                double i = Double.parseDouble(num);
                return num.equals(String.valueOf(i));
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
