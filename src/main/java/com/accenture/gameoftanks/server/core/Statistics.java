package com.accenture.gameoftanks.server.core;

import com.accenture.gameoftanks.core.Player;

import java.sql.*;

public class Statistics {

    private String jdbcDriver = "com.mysql.jdbc.Driver";
    private String dbAddress = "jdbc:mysql://localhost/?useSSL=false";
    private String userName = "root";
    private String password = "abcd1234";
    private PreparedStatement setStatement;
    private PreparedStatement getStatement;
    private Statement statement;
    private ResultSet resultSet;
    private Connection con;

    public Statistics() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(dbAddress, userName, password);
            statement = con.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS GoT_Statistics;");
            con.setCatalog("GoT_Statistics");
            statement = con.createStatement();
            getStatement = con.prepareStatement("SELECT nickname FROM GoT_Statistics.Stats WHERE nickname=?");
            setStatement = con.prepareStatement("UPDATE GoT_Statistics.Stats SET ?=? WHERE nickname=?");
            con.setAutoCommit(false);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Exception in Statistics class constructor");
        }
    }

    public void makeTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Stats " +
                "(id INTEGER not NULL AUTO_INCREMENT," +
                " nickname VARCHAR(255)," +
                " kills INTEGER," +
                " deaths INTEGER," +
                " score INTEGER," +
                " PRIMARY KEY ( id ))";
        try {
            statement.execute(sql);
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            e.printStackTrace();
            System.out.println("Player with a nickname " + player.getNickname() + " not found.");
        }
        return null;
    }

    public void addPlayer(Player player) {
        String nickname = player.getNickname();
        if (nickname.equals(getNickname(player))) {
            System.out.println("Player with a nickname " + nickname + " already exists.");
            return;
        }
        System.out.println("Adding player " + nickname + " to database.");
        try {
            PreparedStatement statement = con.prepareStatement("INSERT INTO GoT_Statistics.Stats (nickname, kills, deaths, score) VALUES (?, ?, ?, ?)");
            statement.setString(1, nickname);
            statement.setInt(2, 0);
            statement.setInt(3, 0);
            statement.setInt(4, 0);
            int i = statement.executeUpdate();
            con.commit();
            System.out.println(i + " records inserted");
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

    public static void main(String[] args) {
        System.out.println("Testing");
        Statistics statistics = new Statistics();
        statistics.makeTable();
        Player player = new Player("Adam");
        statistics.addPlayer(player);
//        statistics.closeConnection();
    }

}


//
//    public void addKill(Player player) throws Exception {
//        int kills = this.getKills(player);
//        setStatement.setString(1, "kills");
//        setStatement.setInt(2, ++kills);
//        setStatement.setString(3, player.getNickname());
//        setStatement.executeUpdate();
//        con.commit();
//        this.recalculateScore(player);
//    }
//
//    public void addDeath(Player player) throws Exception {
//        int deaths = this.getDeaths(player);
//        deaths++;
//        setStatement.setString(1, "Deaths");
//        setStatement.setInt(2, deaths);
//        setStatement.setString(3, player.getNickname());
//        setStatement.executeUpdate();
//        con.commit();
//        this.recalculateScore(player);
//    }
//
//    private void recalculateScore(Player player) throws Exception {
//        int kills = this.getKills(player);
//        int deaths = this.getDeaths(player);
//        int score = (int) (kills / deaths * 0.3);
//        score++;
//        setStatement.setString(1, "Score");
//        setStatement.setInt(2, score);
//        setStatement.setString(3, player.getNickname());
//        setStatement.executeUpdate();
//        con.commit();
//    }
//

//
//    public int getKills(Player player) throws Exception {
//        getStatement.setString(1, "Kills");
//        getStatement.setString(2, player.getNickname());
//        resultSet = getStatement.executeQuery();
//        if (resultSet.next()) {
//            return resultSet.getByte("Kills");
//        }
//        return 0;
//    }
//
//    public int getDeaths(Player player) throws Exception {
//        getStatement.setString(1, "Deaths");
//        getStatement.setString(2, player.getNickname());
//        resultSet = getStatement.executeQuery();
//        if (resultSet.next()) {
//            return resultSet.getByte("Deaths");
//        }
//        return 0;
//    }
//
//    public int getScore(Player player) throws Exception {
//        getStatement.setString(1, "Score");
//        getStatement.setString(2, player.getNickname());
//        resultSet = getStatement.executeQuery();
//        if (resultSet.next()) {
//            return resultSet.getByte("Score");
//        }
//        return 0;
//    }