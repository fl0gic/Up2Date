package com.gamerking195.dev.up2date.util;

import com.gamerking195.dev.up2date.Up2Date;
import com.gamerking195.dev.up2date.config.MainConfig;
import com.gamerking195.dev.up2date.update.PluginInfo;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


/**
 * @author Caden Kriese (flogic)
 *
 * Created on 9/8/17
 */

public class UtilSQL {
    private UtilSQL() {}
    private @Getter static UtilSQL instance = new UtilSQL();

    private HikariDataSource dataSource;

    public void init() {
        if (dataSource == null) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://" + MainConfig.getConf().getHostName() + "/" + MainConfig.getConf().getDatabase());
            config.setUsername(MainConfig.getConf().getUsername());
            config.setPassword(MainConfig.getConf().getPassword());
            config.setMaximumPoolSize(MainConfig.getConf().getConnectionPoolSize());
            config.setPoolName("U2D - User DB (" + MainConfig.getConf().getUsername() + "@" + MainConfig.getConf().getHostName() + ")");

            dataSource = new HikariDataSource(config);
        }

        runStatementAsync("CREATE TABLE IF NOT EXISTS TABLENAME " +
                             "(id varchar(6) NOT NULL, " +
                             "name TEXT, " +
                             "author TEXT, " +
                             "description TEXT, " +
                             "version TEXT, " +
                             "premium TEXT, " +
                             "testedversions TEXT, " +
                             "lastupdated TIMESTAMP, " +
                             "PRIMARY KEY(id))");
    }

    public void shutdown() {
        if (dataSource != null)
            dataSource.close();
    }

    /*
     * QUERIES
     */


    /**
     * Executes a query on the user database on a separate thread.
     *
     * @param query The query to be run.
     * @param callback The callback to be called with the result set.
     */
    public void runQueryAsync(String query, Callback callback) {
        final String updatedQuery = query.replace("TABLENAME", MainConfig.getConf().getTablename());

        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(updatedQuery)) {
            Up2Date.getInstance().getFixedThreadPool().submit(() -> {
                try {
                    callback.call(mapResultSetToArrayList(preparedStatement.executeQuery()));
                } catch (Exception ex) {
                    Up2Date.getInstance().systemOutPrintError(ex, "Error occurred while executing query.");
                }
            });
        } catch (Exception ex) {
            Up2Date.getInstance().printError(ex, "Error occurred while running query '" + updatedQuery + "'.");
        }
    }

    /**
     * Executes a query on the user database on the current thread.
     *
     * @param query The query to be executed.
     * @return The result of the query.
     */
    public ArrayList<PluginInfo> runQuerySync(String query) {
        final String updatedQuery = query.replace("TABLENAME", MainConfig.getConf().getTablename());

        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(updatedQuery)) {
            return mapResultSetToArrayList(preparedStatement.executeQuery());
        } catch (Exception ex) {
            Up2Date.getInstance().printError(ex, "Error occurred while running query '" + updatedQuery + "'.");
        }

        return null;
    }

    /*
     * STATEMENTS
     */

    /**
     * Executes an SQL statement on the users database.
     *
     * @apiNote Runs synchronously.
     * @param statement The statement to be executed.
     * @param suppressErrors Should errors be printed to console.
     */
    public void runStatementSync(String statement, boolean suppressErrors) {
        executeStatement(statement, suppressErrors);
    }

    /**
     * Executes an SQL statement on the users database.
     *
     * @apiNote Runs synchronously.
     * @param statement The statement to be executed.
     */
    public void runStatementSync(String statement) {
        executeStatement(statement, false);
    }

    /**
     * Executes an SQL statement on the users database.
     *
     * @apiNote Runs asynchronously.
     * @param suppressErrors Should errors be suppressed or not.
     * @param statement The statement to be executed.
     */
    public void runStatementAsync(String statement, boolean suppressErrors) {
        Up2Date.getInstance().getFixedThreadPool().submit(() -> executeStatement(statement, suppressErrors));
    }

    /**
     * Executes an SQL statement on the users database.
     *
     * @apiNote Runs asynchronously.
     * @param statement The statement to be executed.
     */
    public void runStatementAsync(String statement) {
        Up2Date.getInstance().getFixedThreadPool().submit(() -> executeStatement(statement, false));
    }

    private void executeStatement(String statement, boolean suppressErrors) {
        final String updatedStatement = statement.replace("TABLENAME", MainConfig.getConf().getTablename());
        if (dataSource == null)
            init();

        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(updatedStatement)) {
            preparedStatement.execute();
        } catch (Exception ex) {
            if (suppressErrors)
                return;
            Up2Date.getInstance().systemOutPrintError(ex, "Error occurred while closing connection.");
        }
    }

    /*
     * UTILITIES
     */

    private ArrayList<PluginInfo> mapResultSetToArrayList(ResultSet resultSet) {
        ArrayList<PluginInfo> plugins = new ArrayList<>();

        try {
            if (resultSet != null && !resultSet.isClosed()) {
                while (resultSet.next())
                    plugins.add(new PluginInfo(resultSet.getString("name"), resultSet.getInt("id"), resultSet.getString("description"), resultSet.getString("author"), resultSet.getString("version"), resultSet.getBoolean("premium"), resultSet.getString("testedversions")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return plugins;
    }
}
