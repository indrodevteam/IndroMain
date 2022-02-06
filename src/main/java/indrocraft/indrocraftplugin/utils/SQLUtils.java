package indrocraft.indrocraftplugin.utils;

import java.sql.*;

public class SQLUtils {

    /*
     *  +-----------------------------------------------+
     *  |               SQL GeneralUtils:               |
     *  |    methods for setting data, getting data     |
     *  | as well as some smaller methods like countRows|
     *  +-----------------------------------------------+
     */

    private final SQLConnector connector;

    public SQLUtils(SQLConnector connector) {
        this.connector = connector;
    }

    public Connection getCorrectConn() {
        if (connector.isUseSQLite()) {
            return connector.getSQLiteConnection();
        } else {
            return connector.getMySQLConnection();
        }
    }

    private void disconnector(Connection conn, PreparedStatement ps) {
        try {
            if (ps != null)
                ps.close();
            if (conn != null)
                connector.closeConnection(conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /*
     *  create statements
     */

    /**
     * @param name     What name do you want to u se for the table
     * @param idColumn This is the unique ID column generally 'NAME'
     */
    public void createTable(String name, String idColumn) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getCorrectConn();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS " + name + " (" + idColumn
                    + " VARCHAR(100),PRIMARY KEY (" + idColumn + "))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnector(conn, ps);
        }
    }

    /**
     * @param id        This is the name of the column
     * @param dataType  What data type do u want to use
     * @param tableName The name of the table you want to put the column into
     */
    public void createColumn(String id, String dataType, String tableName) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            if (columnExists(id, tableName)) return;
            conn = getCorrectConn();
            ps = conn.prepareStatement("ALTER TABLE " + tableName + " ADD `" + id + "` "
                    + dataType);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnector(conn, ps);
        }
    }

    /**
     * @param idColumn  What is the ID column used in this table generally "NAME"
     * @param idEquals  What should the id of this row be? What is the name?
     * @param tableName What table dp you want to inset into?
     */
    public void createRow(String idColumn, String idEquals, String tableName) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            if (!rowExists(idColumn, idEquals, tableName)) {
                conn = getCorrectConn();
                ps = conn.prepareStatement("SELECT * FROM " + tableName);
                ResultSet results = ps.executeQuery();
                results.next();
                PreparedStatement ps2 = conn.prepareStatement("INSERT INTO " + tableName + " ("
                        + idColumn + ") VALUES (?)");
                ps2.setString(1, idEquals);
                ps2.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnector(conn, ps);
        }
    }

    /*
     *  delete statements
     */


    public void deleteRow(String idColumn, String idEquals, String tableName) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getCorrectConn();
            ps = conn.prepareStatement("DELETE FROM " + tableName + " WHERE " + idColumn + "=?");
            ps.setString(1, idEquals);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnector(conn, ps);
        }
    }

    /*
     *  get data
     */

    /**
     * @param column    What column is the desired cell in
     * @param idColumn  What is the id column used for this table
     * @param idEquals  What id are you looking for?
     * @param tableName What is the name of the table
     * @return returns the value of a specific cell as an Object if it can't find that cell it will return null
     * @apiNote since it's returned as an Object you need to cast it to the required data type:
     * `String s = (String) object`
     * @apiNote other methods that include getString and more are planed
     */

    public Object getData(String column, String idColumn, String idEquals, String tableName) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getCorrectConn();
            ps = conn.prepareStatement("SELECT " + column + " FROM " + tableName + " WHERE "
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
        } finally {
            disconnector(conn, ps);
        }
        return null;
    }

    @Deprecated
    public String getString(String column, String idColumn, String idEquals, String tableName) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getCorrectConn();
            ps = conn.prepareStatement("SELECT `" + column + "` FROM " + tableName + " WHERE `"
                    + idColumn + "`=?;");
            ps.setString(1, idEquals);
            ResultSet rs = ps.executeQuery();
            String info;
            if (rs.next()) {
                info = rs.getString(column);
                return info;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnector(conn, ps);
        }
        return null;
    }

    @Deprecated
    public Float getFloat(String column, String idColumn, String idEquals, String tableName) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getCorrectConn();
            ps = conn.prepareStatement("SELECT " + column + " FROM " + tableName + " WHERE "
                    + idColumn + "=?");
            ps.setString(1, idEquals);
            ResultSet rs = ps.executeQuery();
            Float info;
            if (rs.next()) {
                info = rs.getFloat(column);
                return info;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnector(conn, ps);
        }
        return null;
    }

    @Deprecated
    public Double getDouble(String column, String idColumn, String idEquals, String tableName) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getCorrectConn();
            ps = conn.prepareStatement("SELECT " + column + " FROM " + tableName + " WHERE "
                    + idColumn + "=?");
            ps.setString(1, idEquals);
            ResultSet rs = ps.executeQuery();
            Double info;
            if (rs.next()) {
                info = rs.getDouble(column);
                return info;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnector(conn, ps);
        }
        return null;
    }

    @Deprecated
    public Integer getInt(String column, String idColumn, String idEquals, String tableName) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getCorrectConn();
            ps = conn.prepareStatement("SELECT " + column + " FROM " + tableName + " WHERE "
                    + idColumn + "=?");
            ps.setString(1, idEquals);
            ResultSet rs = ps.executeQuery();
            Integer info;
            if (rs.next()) {
                info = rs.getInt(column);
                return info;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnector(conn, ps);
        }
        return null;
    }

    /*
     *  set data
     */

    /**
     * @param value     Data to be saved in form of a string, number will be converted depending on dataType
     * @param idColumn  identifier column to check
     * @param id        String that the id column is checked against
     * @param column    column to insert data
     * @param tableName table to insert data
     */
    public void setData(Object value, String idColumn, String id, String column, String tableName) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getCorrectConn();
            ps = conn.prepareStatement("UPDATE " + tableName + " SET `" + column + "`=? WHERE "
                    + idColumn + "=?");
            ps.setObject(1, value);
            ps.setString(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnector(conn, ps);
        }
    }

    /*
     *  other
     */

    /**
     * @param idColumn  What column in this table is unique generally the "NAME" column
     * @param test      What do u want to test the idColumn for
     * @param tableName In what table
     * @return Returns true if it exists and false if it does not
     */
    public boolean rowExists(String idColumn, String test, String tableName) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getCorrectConn();
            ps = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE " + idColumn
                    + "=?");
            ps.setString(1, test);

            ResultSet results = ps.executeQuery();
            //row is found
            return results.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnector(conn, ps);
        }
        return false;
    }

    /**
     * @param name      Name of the column you are checking for
     * @param tableName name of the target table
     * @return returns true if the column exists else returns false
     */
    public boolean columnExists(String name, String tableName) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getCorrectConn();
            if (connector.isUseSQLite()) {
                ps = conn.prepareStatement("SELECT COUNT(*) AS CNTREC FROM pragma_table_info(?) WHERE name=?");
                ps.setString(1, tableName);
                ps.setString(2, name);
                ResultSet resultSet = ps.executeQuery();
                return resultSet.getInt(1) == 1;
            } else {
                ps = conn.prepareStatement("show columns from " + tableName +
                        " where field = ?");
                ps.setString(1, name);
                ResultSet resultSet = ps.executeQuery();
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnector(conn, ps);
        }
        return false;
    }

    /**
     * @param query      SQL query to be executed
     * @param parameters parameters for the query in the order that they should be inserted
     */
    public void makeQuery(String query, Object... parameters) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getCorrectConn();
            ps = conn.prepareStatement(query);
            int index = 1;
            for (Object param : parameters) {
                ps.setObject(index, param);
                index++;
            }
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnector(conn, ps);
        }
    }
}