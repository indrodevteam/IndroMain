package io.github.indrodevteam.indroMain.data;

import com.mysql.cj.jdbc.MysqlDataSource;
import io.github.indrodevteam.indroMain.IndroMain;
import org.bukkit.configuration.file.FileConfiguration;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Logger;

public class DataSourceFactory {
    private final DataSource daso;
    private static final Logger LOGGER = IndroMain.getInstance().getLogger();

    DataSourceFactory() {
        MysqlDataSource daso = new MysqlDataSource();
        FileConfiguration config = IndroMain.getInstance().getConfig();

        daso.setDatabaseName(config.getString("mysql.database"));
        daso.setServerName(config.getString("mysql.serverName"));
        daso.setPort(config.getInt("mysql.port"));
        daso.setUser(config.getString("mysql.user"));
        daso.setPassword(config.getString("mysql.password"));

        this.daso = daso;
    }

    public static Connection getConnection() throws SQLException {
        return SingletonHelper.INSTANCE.daso.getConnection();
    }

    private static class SingletonHelper {
        private static final DataSourceFactory INSTANCE = new DataSourceFactory();
    }
}
