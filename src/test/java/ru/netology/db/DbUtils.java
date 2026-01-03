package ru.netology.db;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtils {
    private DbUtils() {}

    private static final String URL =
            "jdbc:mysql://localhost:3306/app?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "app";
    private static final String PASS = "pass";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }


    public static void cleanAuthCodes() {
        QueryRunner runner = new QueryRunner();
        try (Connection conn = getConnection()) {
            runner.update(conn, "DELETE FROM auth_codes");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

   public static String getAuthCode(String login) {
        QueryRunner runner = new QueryRunner();
        String sql =
                "SELECT ac.code " +
                        "FROM auth_codes ac " +
                        "JOIN users u ON ac.user_id = u.id " +
                        "WHERE u.login = ? " +
                        "ORDER BY ac.created DESC " +
                        "LIMIT 1";

        try (Connection conn = getConnection()) {
            return runner.query(conn, sql, new ScalarHandler<>(), login);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static String waitAuthCode(String login, int attempts, long sleepMs) {
        for (int i = 0; i < attempts; i++) {
            String code = getAuthCode(login);
            if (code != null) return code;
            try {
                Thread.sleep(sleepMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
        return null;
    }


    public static void cleanAllTestData() {
        QueryRunner runner = new QueryRunner();
        try (Connection conn = getConnection()) {
            // порядок удаления: дочерние таблицы -> родительские
            runner.update(conn, "DELETE FROM card_transactions");
            runner.update(conn, "DELETE FROM auth_codes");
            runner.update(conn, "DELETE FROM cards");
            runner.update(conn, "DELETE FROM users"); // последняя!
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}