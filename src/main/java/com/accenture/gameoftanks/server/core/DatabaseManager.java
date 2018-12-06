package com.accenture.gameoftanks.server.core;

import com.accenture.gameoftanks.core.Player;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DatabaseManager {

    private String dbAddress = "jdbc:mysql://localhost/?useSSL=false";
    //    private String userName = "root";
    //    private String password = "abcd1234";
    private final String logStatementString = "INSERT INTO Game_of_Tanks.logs (nickname, status, time) VALUES (?, ?, ?)";
    private Connection con;
//    private ResultSet resultSet;

    public DatabaseManager(String username, String password) {
        try {
            con = DriverManager.getConnection(dbAddress, username, password);
            Statement statement = con.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS Game_of_Tanks;");
            statement.close();
            con.setCatalog("Game_of_Tanks");
            con.setAutoCommit(false);
            createStatsTable();
            createLogsTable();
        } catch (SQLException e) {
            System.out.println("Exception in DatabaseManager class constructor");
            e.printStackTrace();
        }
    }

    public void logConnect(Player player) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement preparedStatement = con.prepareStatement(logStatementString);
                    preparedStatement.setString(1, player.getNickname());
                    preparedStatement.setString(2, "connected");
                    preparedStatement.setString(3, LocalDateTime.now().toString());
                    preparedStatement.executeUpdate();
                    con.commit();
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void logDisconnect(Player player) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement preparedStatement = con.prepareStatement(logStatementString);
                    preparedStatement.setString(1, player.getNickname());
                    preparedStatement.setString(2, "disconnected");
                    preparedStatement.setString(3, LocalDateTime.now().toString());
                    preparedStatement.executeUpdate();
                    con.commit();
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public List<String> getLogs() {
        List<String> linkedList = new LinkedList();
        try {
            System.out.println("Generating logs");
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT DISTINCT nickname, status, time FROM Game_of_Tanks.logs");
            while (resultSet.next()) {
                String string = "" + resultSet.getString("nickname") + " " + resultSet.getString("status") + " in " + resultSet.getString("time");
                linkedList.add(string);
            }
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return linkedList;
    }

    public void printLogs() {
        List<String> linkedList = getLogs();
        for (String string : linkedList) {
            System.out.println(string);
        }
    }

    private void createStatsTable() {
        String string = "CREATE TABLE IF NOT EXISTS stats " +
                "(id INTEGER not NULL AUTO_INCREMENT," +
                " nickname VARCHAR(255)," +
                " kills INTEGER," +
                " deaths INTEGER," +
                " score INTEGER," +
                " PRIMARY KEY ( id ))";
        try {
            Statement statement = con.createStatement();
            statement.execute(string);
            con.commit();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createLogsTable() {
        String string = "CREATE TABLE IF NOT EXISTS logs " +
                "(id INTEGER not NULL AUTO_INCREMENT, " +
                " nickname VARCHAR(255)," +
                " status VARCHAR(255)," +
                " time VARCHAR(255)," +
                " PRIMARY KEY ( id ))";
        try {
            Statement statement = con.createStatement();
            statement.execute(string);
            con.commit();
            statement.close();
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

    public void addOrUpdatePlayer(Player player) {
        new Thread((new Runnable() {
            @Override
            public void run() {
                if (playerExists(player)) {
                    updatePlayer(player);
                } else {
                    addPlayer(player);
                }
            }
        })).start();
    }

    private void updatePlayer(Player player) {
        try {
            System.out.println("Adding " + player.getNickname() + "'s values to databse.");
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE Game_of_Tanks.stats SET kills = ?, deaths = ? WHERE nickname = ?");
            preparedStatement.setInt(1, getKills(player) + player.getKills());
            preparedStatement.setInt(2, getDeaths(player) + player.getDeaths());
            preparedStatement.setString(3, player.getNickname());
            preparedStatement.executeUpdate();
            con.commit();
            preparedStatement.close();
            setScore(player);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addPlayer(Player player) {
        try {
            System.out.println("Adding player " + player.getNickname() + " to database.");
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO Game_of_Tanks.stats (nickname, kills, deaths) VALUES (?, ?, ?)");
            preparedStatement.setString(1, player.getNickname());
            preparedStatement.setInt(2, player.getKills());
            preparedStatement.setInt(3, player.getDeaths());
            preparedStatement.executeUpdate();
            con.commit();
            preparedStatement.close();
            setScore(player);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getNickname(Player player) {
        String nickname = null;
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT nickname FROM Game_of_Tanks.stats WHERE nickname=?");
            preparedStatement.setString(1, player.getNickname());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                nickname = resultSet.getString("nickname");
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("Player with a nickname " + player.getNickname() + " not found.");
            e.printStackTrace();
        }
        return nickname;
    }

    public int getKills(Player player) {
        int kills = 0;
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT kills FROM Game_of_Tanks.stats WHERE nickname=?");
            preparedStatement.setString(1, player.getNickname());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                kills = resultSet.getByte("kills");
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("Player with a nickname " + player.getNickname() + " not found.");
            e.printStackTrace();
        }
        return kills;
    }

    public int getDeaths(Player player) {
        int deaths = 0;
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT deaths FROM Game_of_Tanks.stats WHERE nickname=?");
            preparedStatement.setString(1, player.getNickname());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                deaths = resultSet.getByte("deaths");
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("Player with a nickname " + player.getNickname() + " not found.");
            e.printStackTrace();
        }
        return deaths;
    }

    public int getScore(Player player) {
        int score = 0;
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT score FROM Game_of_Tanks.stats WHERE nickname=?");
            preparedStatement.setString(1, player.getNickname());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                score = resultSet.getByte("score");
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("Player with a nickname " + player.getNickname() + " not found.");
            e.printStackTrace();
        }
        return score;
    }

    public void setKills(Player player) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE Game_of_Tanks.stats SET kills = ? WHERE nickname = ?");
            preparedStatement.setInt(1, player.getKills());
            preparedStatement.setString(2, player.getNickname());
            preparedStatement.executeUpdate();
            con.commit();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDeaths(Player player) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE Game_of_Tanks.stats SET deaths = ? WHERE nickname = ?");
            preparedStatement.setInt(1, player.getDeaths());
            preparedStatement.setString(2, player.getNickname());
            con.commit();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setScore(Player player) {
        int deaths = getDeaths(player);
        int kills = getKills(player);
        int score = 0;

        if (kills != 0) {
            score = (int) (kills / (0.8 * deaths));
        }

        try {
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE Game_of_Tanks.stats SET score = ? WHERE nickname = ?");
            preparedStatement.setInt(1, score);
            preparedStatement.setString(2, player.getNickname());
            preparedStatement.executeUpdate();
            con.commit();
            preparedStatement.close();
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

    //TODO sort the hashmap in descending order by scores and leave only top 10 players
    private Map<String, Map<String, String>> returnPlayerStats() {
        Map<String, Map<String, String>> players = new HashMap<>();
        Map<String, Map<String, String>> topTen = new HashMap<>();

        String string = "SELECT nickname, kills, deaths, score FROM Game_of_Tanks.stats";
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(string);
            while (resultSet.next()) {
                Map<String, String> stats = new HashMap<>();
                String nickname = resultSet.getString("nickname");
                int kills = resultSet.getByte("kills");
                int deaths = resultSet.getByte("deaths");
                int score = resultSet.getByte("score");
                stats.put("kills", Integer.toString(kills));
                stats.put("deaths", Integer.toString(deaths));
                stats.put("score", Integer.toString(score));
                players.put(nickname, stats);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        topTen = players;
        return topTen;
    }

//    public static void main(String[] args) {
//
//        DatabaseManager db = new DatabaseManager("root", "abcd1234");
//
//        Player player1 = new Player("abc");
//        Player player2 = new Player("zxc");
//        Player player3 = new Player("qwe");
//        Player player4 = new Player("aoe");
//
//        db.addOrUpdatePlayer(player1);
//        db.logConnect(player1);
//        db.addOrUpdatePlayer(player2);
//        db.logConnect(player2);
//        db.addOrUpdatePlayer(player3);
//        db.logConnect(player3);
//        db.addOrUpdatePlayer(player4);
//        db.logConnect(player4);
//        db.logDisconnect(player1);
//        db.logDisconnect(player2);
//        db.logDisconnect(player4);
//        db.printLogs();
//        System.out.println("Success");
//    }

}