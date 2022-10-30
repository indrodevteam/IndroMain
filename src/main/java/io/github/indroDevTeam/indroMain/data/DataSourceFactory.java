package io.github.indroDevTeam.indroMain.data;

import io.github.indroDevTeam.indroMain.IndroMain;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DataSourceFactory {
    private final DataSource daso;
    private static final Logger LOGGER = IndroMain.getInstance().getLogger();

    DataSourceFactory() {
        SQLiteDataSource daso = new SQLiteDataSource();
        File file = new File(IndroMain.getInstance().getDataFolder() + File.separator + "data.db");
        daso.setUrl("jdbc:sqlite:" + file.getAbsolutePath());

        this.daso = daso;
    }

    public static Connection getConnection() throws SQLException {
        return SingletonHelper.INSTANCE.daso.getConnection();
    }

    private static class SingletonHelper {
        private static final DataSourceFactory INSTANCE = new DataSourceFactory();
    }
}
