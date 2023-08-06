package Electricity;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbConnection implements AutoCloseable {
    private static final String DB_URL = "jdbc:mysql:///ebs";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "radhasoami";

    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);
        dataSource = new HikariDataSource(config);
    }

    private static Connection connection;

    public DbConnection() throws SQLException {
        this.connection = dataSource.getConnection();
    }

    public static Connection getConnection() {
        return connection;
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    public ResultSet executeQuery(String query) throws SQLException {
        return connection.createStatement().executeQuery(query);
    }

    public int executeUpdate(String query) throws SQLException {
        return connection.createStatement().executeUpdate(query);
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}