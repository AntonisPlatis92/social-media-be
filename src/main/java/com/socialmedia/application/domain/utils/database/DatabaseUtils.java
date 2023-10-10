package com.socialmedia.application.domain.utils.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtils {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres"; // PostgreSQL URL
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "password";

    private static final ThreadLocal<Connection> threadLocalConnection = ThreadLocal.withInitial(() -> null);

    public static void doInTransaction(DatabaseAction action) {
        var conn = threadLocalConnection.get();
        var newTransaction = false;

        try {
            if (conn == null) {
                // Load the PostgreSQL JDBC driver (org.postgresql.Driver)
                Class.forName("org.postgresql.Driver");
                conn = getConnection();
                conn.setAutoCommit(false);
                threadLocalConnection.set(conn);
                newTransaction = true;
            }

            action.execute(conn);

            if (newTransaction) {
                conn.commit();
            }
        } catch (Exception e) {
            if (conn != null) {
                rollbackTransaction(conn);
            }
        } finally {
            if (newTransaction) {
                threadLocalConnection.remove();
                closeConnection(conn);
            }
        }
    }

    public static  <T> T doInTransactionAndReturn(DatabaseFunction<T> action) {
        Connection conn = threadLocalConnection.get();
        boolean newTransaction = false;

        try {
            if (conn == null) {
                // Load the PostgreSQL JDBC driver (org.postgresql.Driver)
                Class.forName("org.postgresql.Driver");
                conn = getConnection();
                conn.setAutoCommit(false);
                threadLocalConnection.set(conn);
                newTransaction = true;
            }

            T result = action.execute(conn);

            if (newTransaction) {
                conn.commit();
            }

            return result;
        } catch (Exception e) {
            if (newTransaction) {
                rollbackTransaction(conn);
            }
            return null;
        } finally {
            if (newTransaction) {
                threadLocalConnection.remove();
                closeConnection(conn);
            }
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private static void rollbackTransaction(Connection conn) {
        try {
            conn.rollback(); // Rollback the transaction
        } catch (SQLException e) {
            throw new RuntimeException("Failed to rollback", e);
        }
    }

    private static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to close connection", e);
            }
        }
    }

    @FunctionalInterface
    public interface DatabaseAction {
        void execute(Connection connection) throws SQLException;
    }

    @FunctionalInterface
    public interface DatabaseFunction<T> {
        T execute(Connection connection) throws SQLException;
    }
}
