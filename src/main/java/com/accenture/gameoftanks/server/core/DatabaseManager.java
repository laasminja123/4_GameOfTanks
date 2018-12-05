package com.accenture.gameoftanks.server.core;

import com.accenture.gameoftanks.core.Player;

import java.sql.*;
import java.time.LocalDateTime;

public class DatabaseManager {

    private String dbAddress = "jdbc:mysql://localhost/?useSSL=false";
    //    private String userName = "root";
    //    private String password = "abcd1234";
    private Connection con;
    private Statement statement;
    private PreparedStatement getStatement;
    private PreparedStatement setStatement;
    private PreparedStatement logStatement;
    private ResultSet resultSet;

    public DatabaseManager(String username, String password) {
        try {
            con = DriverManager.getConnection(dbAddress, username, password);
            statement = con.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS Game_of_Tanks;");
            con.setCatalog("GoT_Statistics");
            statement = con.createStatement();
            con.setAutoCommit(false);

            getStatement = con.prepareStatement("SELECT nickname FROM Game_of_Tanks.stats WHERE nickname=?");
            setStatement = con.prepareStatement("UPDATE Game_of_Tanks.stats SET ?=? WHERE nickname=?");
            logStatement = con.prepareStatement("INSERT INTO Game_of_Tanks.logs (nickname, status, time) VALUES (?, ?, ?)");
            createStatsTable();
            createLogsTable();
        } catch (SQLException e) {
            System.out.println("Exception in DatabaseManager class constructor");
            e.printStackTrace();
        }
    }

    public void logConnect(Player player) {
        String time = "";
        try {
            logStatement.setString(1, player.getNickname());
            logStatement.setString(2, "connected");
            logStatement.setString(3, time);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void logDisconnect(Player player) {
        String time = "";
        try {
            logStatement.setString(1, player.getNickname());
            logStatement.setString(2, "disconnected");
            logStatement.setString(3, time);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createStatsTable() {
        String string = "CREATE TABLE IF NOT EXISTS stats " +
                "(id INTEGER not NULL AUTO_INCREMENT," +
                " nickname VARCHAR(255)," +
                " kills INTEGER," +
                " deaths INTEGER," +
                " score INTEGER," +
                " PRIMARY KEY ( id ))";
        try {
            statement.execute(string);
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createLogsTable() {
        String string = "CREATE TABLE IF NOT EXISTS logs " +
                "(id INTEGER not NULL AUTO_INCREMENT, " +
                " nickname VARCHAR(255)," +
                " status VARCHAR(255)," +
                " time VARCHAR(255)," +
                " PRIMARY KEY ( id ))";
        try {
            statement.execute(string);
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean playerExists(Player player) {
        String nickname = player.getNickname();
        if (nickname.equals(getNickname(player))) {
            System.out.println("Player with a nickname " + nickname + " already exists.");
            return true;
        }
        return false;
    }

    public void AddOrUpdatePlayer(Player player) {
        if (playerExists(player)) {
            updatePlayer(player);
        } else {
            addPlayer(player);
        }
    }

    public void updatePlayer(Player player) {
        try {
            System.out.println("Adding " + player.getNickname() + "'s values to databse.");
            PreparedStatement statement = con.prepareStatement("UPDATE Game_of_Tanks.stats SET kills = ?, deaths = ? WHERE nickname = ?");
            statement.setInt(1, getKills(player) + player.getKills());
            statement.setInt(2, getDeaths(player) + player.getDeaths());
            statement.setString(3, player.getNickname());
            statement.executeUpdate();
            con.commit();
            setScore(player);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPlayer(Player player) {
        try {
            System.out.println("Adding player " + player.getNickname() + " to database.");
            PreparedStatement statement = con.prepareStatement("INSERT INTO Game_of_Tanks.stats (nickname, kills, deaths) VALUES (?, ?, ?)");
            statement.setString(1, player.getNickname());
            statement.setInt(2, player.getKills());
            statement.setInt(3, player.getDeaths());
            statement.executeUpdate();
            con.commit();
            setScore(player);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        con = null;
    }

    public String getNickname(Player player) {
        String string = player.getNickname();
        try {
            getStatement.setString(1, string);
            resultSet = getStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("nickname");
            }
        } catch (SQLException e) {
            System.out.println("Player with a nickname " + player.getNickname() + " not found.");
            e.printStackTrace();
        }
        return null;
    }

    public int getKills(Player player) {
        try {
            getStatement.setString(1, "kills");
            getStatement.setString(2, player.getNickname());
            resultSet = getStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getByte("kills");
            }
        } catch (SQLException e) {
            System.out.println("Player with a nickname " + player.getNickname() + " not found.");
            e.printStackTrace();
        }
        return 0;
    }

    public int getDeaths(Player player) {
        try {
            getStatement.setString(1, "deaths");
            getStatement.setString(2, player.getNickname());
            resultSet = getStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getByte("deaths");
            }
        } catch (SQLException e) {
            System.out.println("Player with a nickname " + player.getNickname() + " not found.");
            e.printStackTrace();
        }
        return 0;
    }

    public int getScore(Player player) {
        try {
            getStatement.setString(1, "score");
            getStatement.setString(2, player.getNickname());
            resultSet = getStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getByte("score");
            }
        } catch (SQLException e) {
            System.out.println("Player with a nickname " + player.getNickname() + " not found.");
            e.printStackTrace();
        }
        return 0;
    }

    public void setKills(Player player) {
        try {
            setStatement.setString(1, "kills");
            setStatement.setInt(2, player.getKills());
            setStatement.setString(3, player.getNickname());
            setStatement.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDeaths(Player player) {
        try {
            setStatement.setString(1, "deaths");
            setStatement.setInt(2, player.getDeaths());
            setStatement.setString(3, player.getNickname());
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setScore(Player player) {
        int deaths = getDeaths(player);
        int kills = getKills(player);
        int score = (int) (kills / (0.8 * deaths));
        try {
            setStatement.setString(1, "score");
            setStatement.setInt(2, score);
            setStatement.setString(3, player.getNickname());
            setStatement.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LocalDateTime time = LocalDateTime.now();
        String string = time.toString();
        System.out.println(string);
    }

}