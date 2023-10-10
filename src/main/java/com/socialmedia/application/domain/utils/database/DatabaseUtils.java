package com.socialmedia.application.domain.utils.database;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseUtils {
    private static final HikariDataSource dataSource = HikariCPDataSource.getDataSource();
    private static final ThreadLocal<Connection> threadLocalConnection = ThreadLocal.withInitial(() -> null);

    public static void doInTransaction(DatabaseAction action) {
        Connection conn = threadLocalConnection.get();
        boolean newTransaction = false;

        try {
            if (conn == null) {
                conn = getConnection(); // Reuse the existing data source
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
        return dataSource.getConnection();
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
