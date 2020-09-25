package server;

import java.sql.*;

public class Sql {
    private static Connection connection;
    private static PreparedStatement psSelect;
    private static PreparedStatement psInsert;
    private static PreparedStatement psUpdate;

    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:main.db");
        preparedStatements();
    }

    private static void preparedStatements() throws SQLException {
        psSelect = connection.prepareStatement("SELECT nickname FROM users WHERE login = ? AND password = ?;");
        psInsert = connection.prepareStatement("INSERT INTO users(login, password, nickname) VALUES (? ,? ,? );");
        psUpdate = connection.prepareStatement("UPDATE users SET nickname = ? WHERE nickname = ?;");
    }

    public static void disconnect() {
        try {
            psSelect.close();
            psInsert.close();
            psUpdate.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean registration(String login, String password, String nickname) {
        try {
            psInsert.setString(1, login);
            psInsert.setString(2, password);
            psInsert.setString(3, nickname);
            psInsert.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean changeNick(String oldNickname, String newNickname) {
        try {
            psUpdate.setString(1, newNickname);
            psUpdate.setString(2, oldNickname);
            psUpdate.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static String getNicknameByLoginAndPassword(String login, String password) {
        String nick = null;
        try {
            psSelect.setString(1, login);
            psSelect.setString(2, password);
            ResultSet rs = psSelect.executeQuery();
            if (rs.next()) {
                nick = rs.getString("nickname");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nick;
    }
}
