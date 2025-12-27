package ru.netology.db;

import java.sql.*;

public class DbUtils {

    private static final String URL =
            "jdbc:mysql://localhost:3306/app?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "app";
    private static final String PASS = "pass";

    public static void cleanDatabase() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement st = conn.createStatement()) {
            st.execute("SET FOREIGN_KEY_CHECKS = 0");
            st.execute("TRUNCATE TABLE auth_codes");
            st.execute("SET FOREIGN_KEY_CHECKS = 1");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getAuthCode(String login) {
        String sql =
                "SELECT ac.code " +
                        "FROM auth_codes ac " +
                        "JOIN users u ON ac.user_id = u.id " +
                        "WHERE u.login = ? " +
                        "ORDER BY ac.created DESC " +
                        "LIMIT 1";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, login);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return rs.getString("code");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
