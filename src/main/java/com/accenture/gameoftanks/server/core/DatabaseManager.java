package com.accenture.gameoftanks.server.core;

import com.accenture.gameoftanks.core.Player;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class DatabaseManager {

    private String dbAddress = "jdbc:mysql://localhost/?useSSL=false";
    //    private String userName = "root";
    //    private String password = "abcd1234";
    private final String logStatementString = "INSERT INTO Game_of_Tanks.logs (nickname, status, time) VALUES (?, ?, ?)";
    private Connection con;
//    private ResultSet resultSet;

    public DatabaseManager(String username, String password) throws SQLException {
            con = DriverManager.getConnection(dbAddress, username, password);
    }

    public void setupDatabase() {
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS Game_of_Tanks;");
            statement.close();
            con.setCatalog("Game_of_Tanks");
            con.setAutoCommit(false);
            createStatsTable();
            createLogsTable();
        } catch (SQLException e) {
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
            System.out.println("\nGenerating logs:\n");
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
                " shoots INTEGER," +
                " hits INTEGER," +
                " accuracy INTEGER," +
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
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE Game_of_Tanks.stats SET kills = ?, deaths = ?, shoots = ?, hits = ? WHERE nickname = ?");
            preparedStatement.setInt(1, getKills(player) + player.getKills());
            preparedStatement.setInt(2, getDeaths(player) + player.getDeaths());
            preparedStatement.setInt(3, getShoots(player.getNickname()) + player.getShoots());
            preparedStatement.setInt(4, getHits(player.getNickname()) + player.getHits());
            preparedStatement.setString(5, player.getNickname());
            preparedStatement.executeUpdate();
            con.commit();
            preparedStatement.close();
            setScore(player);
            updateAccuracy(player);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addPlayer(Player player) {
        try {
            System.out.println("Adding player " + player.getNickname() + " to database.");
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO Game_of_Tanks.stats (nickname, kills, deaths, shoots, hits, accuracy) VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, player.getNickname());
            preparedStatement.setInt(2, player.getKills());
            preparedStatement.setInt(3, player.getDeaths());
            preparedStatement.setInt(4, player.getShoots());
            preparedStatement.setInt(5, player.getHits());
            preparedStatement.setInt(6, calculateAccuracy(player));
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
            score = (kills - deaths);
//            score = (int) ((kills * 3) / (0.3 * deaths));
        }

        if (score < 0) {
            score = 0;
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

    public List<Map<String, String>> returnTopPlayerStats() {
        List<Map<String, String>> players = new LinkedList<>();

        String string = "SELECT nickname, kills, deaths, score, shoots, hits, accuracy FROM Game_of_Tanks.stats ORDER BY score DESC";
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(string);

            int i = 0;
            while (resultSet.next() && i < 10) {
                Map<String, String> player = new HashMap<>();
                String nickname = resultSet.getString("nickname");
                int kills = resultSet.getByte("kills");
                int deaths = resultSet.getByte("deaths");
                int score = resultSet.getByte("score");
                int shoots = resultSet.getByte("shoots");
                int hits = resultSet.getByte("hits");
                int accuracy = resultSet.getByte("accuracy");
                player.put("nickname", nickname);
                player.put("kills", Integer.toString(kills));
                player.put("deaths", Integer.toString(deaths));
                player.put("shoots", Integer.toString(shoots));
                player.put("hits", Integer.toString(hits));
                player.put("accuracy", Integer.toString(accuracy));
                player.put("score", Integer.toString(score));
                players.add(player);
                i++;
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return players;
    }

    public void printTopPlayers() {
        List<Map<String, String>> topPlayers = returnTopPlayerStats();
        System.out.println("\nShowing top 10 players ordered by score\n");

        for (Map<String, String> map : topPlayers) {
            String nickname = map.get("nickname");
            String kills = map.get("kills");
            String deaths = map.get("deaths");
            String score = map.get("score");
            String shoots = map.get("shoots");
            String hits = map.get("hits");
            String accuracy = map.get("accuracy");
            System.out.println(nickname + " " + kills + "-" + deaths + " score:" + score + " accuracy:" + accuracy +"%" + " (shoots:" + shoots + " hits:" + hits +")");
        }
    }

    public void updateAccuracy(Player player) {
        int shoots = getShoots(player.getNickname());
        int hits = getHits(player.getNickname());
        float accuracy = 0;
        if (shoots > 0) {
            accuracy = (hits * 1.0f) / (shoots * 1.0f);
        }
        int intValue = (int) (accuracy * 100);

        try {
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE Game_of_Tanks.stats SET accuracy = ? WHERE nickname = ?");
            preparedStatement.setInt(1, intValue);
            preparedStatement.setString(2, player.getNickname());
            preparedStatement.executeUpdate();
            con.commit();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private int calculateAccuracy(Player player) {
        float accuracy = 0;
        if (player.getShoots() > 0) {
            accuracy = (player.getHits() * 1.0f) / (player.getShoots() * 1.0f);
        }
        int intValue = (int) (accuracy * 100);
        return intValue;
    }

    public void setShoots(Player player) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE Game_of_Tanks.stats SET shoots = ? WHERE nickname = ?");
            preparedStatement.setInt(1, player.getShoots());
            preparedStatement.setString(2, player.getNickname());
            preparedStatement.executeUpdate();
            con.commit();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setHits(Player player) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE Game_of_Tanks.stats SET hits = ? WHERE nickname = ?");
            preparedStatement.setInt(1, player.getHits());
            preparedStatement.setString(2, player.getNickname());
            preparedStatement.executeUpdate();
            con.commit();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getAccuracy(String nick) {
        int accuracy = 0;
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT accuracy FROM Game_of_Tanks.stats WHERE nickname=?");
            preparedStatement.setString(1, nick);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                accuracy = resultSet.getByte("accuracy");
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("Player with a nickname " + nick + " not found.");
            e.printStackTrace();
        }
        return accuracy;
    }

    public int getShoots(String nick) {
        int shoots = 0;
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT shoots FROM Game_of_Tanks.stats WHERE nickname=?");
            preparedStatement.setString(1, nick);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                shoots = resultSet.getByte("shoots");
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("Player with a nickname " + nick + " not found.");
            e.printStackTrace();
        }
        return shoots;
    }

    public int getHits(String nick) {
        int hits = 0;
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT hits FROM Game_of_Tanks.stats WHERE nickname=?");
            preparedStatement.setString(1, nick);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                hits = resultSet.getByte("hits");
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("Player with a nickname " + nick + " not found.");
            e.printStackTrace();
        }
        return hits;
    }

//    public static void main(String[] args) {
//
//        DatabaseManager db = null;
//        try {
//            db = new DatabaseManager("root", "abcd1234");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        db.setupDatabase();
//
//        Player player1 = new Player("abc");
//        Player player2 = new Player("zxc");
//        Player player3 = new Player("qwe");
//        Player player4 = new Player("aoe");
//        Player player5 = new Player("zxc1");
//        Player player6 = new Player("zxc2");
//        Player player7 = new Player("zxc3");
//        Player player8 = new Player("qwe4");
//        Player player9 = new Player("qwe5");
//
//        player1.incrementKills();
//        player1.incrementKills();
//        player1.incrementKills();
//        player1.incrementKills();
//        player1.incrementKills();
//        player2.incrementDeaths();
//        player2.incrementDeaths();
//        player2.incrementDeaths();
//        player2.incrementDeaths();
//        player3.incrementKills();
//        player3.incrementKills();
//        player3.incrementKills();
//        player3.incrementDeaths();
//        player3.incrementDeaths();
//        player1.incrementShoots();
//        player1.incrementShoots();
//        player1.incrementShoots();
//        player1.incrementShoots();
//        player1.incrementShoots();
//        player1.incrementShoots();
//        player1.incrementShoots();
//        player1.incrementShoots();
//        player1.incrementShoots();
//        player1.incrementShoots();
//        player1.incrementShoots();
//        player1.incrementHits();
//        player1.incrementHits();
//        player1.incrementHits();
//        player1.incrementHits();
//        player1.incrementHits();
//        player1.incrementHits();
//        player1.incrementHits();
//        player1.incrementHits();
//        player1.incrementHits();
//        player1.incrementHits();
//        player2.incrementShoots();
//        player2.incrementShoots();
//        player2.incrementHits();
//        player3.incrementShoots();
//        player3.incrementHits();
//        player3.incrementKills();
//
//        db.addOrUpdatePlayer(player1);
//        db.addOrUpdatePlayer(player2);
//        db.addOrUpdatePlayer(player3);
//        db.addOrUpdatePlayer(player4);
//        db.addOrUpdatePlayer(player5);
//        db.addOrUpdatePlayer(player6);
//        db.addOrUpdatePlayer(player7);
//        db.addOrUpdatePlayer(player8);
//        db.addOrUpdatePlayer(player9);
//
//        int i = db.getShoots(player1.getNickname());
//        int x = db.getHits(player1.getNickname());
//        System.err.println(i + " TEXT");
//        System.err.println(x + " TEXT");
//    }
}
