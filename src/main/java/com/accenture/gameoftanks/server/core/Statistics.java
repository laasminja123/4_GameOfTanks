package com.accenture.gameoftanks.server.core;

import com.accenture.gameoftanks.core.Player;

import java.sql.*;

public class Statistics {

    private String jdbcDriver = "com.mysql.jdbc.Driver";
//    private String dbAddress = "jdbc:mysql://localhost:3306/Statistics?createDatabaseIfNotExists=true";
    private String dbAddress = "jdbc:mysql://localhost/?autoReconnect=true&useSSL=false";
    private String userName = "root";
    private String password ="abcd";
    private PreparedStatement setStatement;
    private PreparedStatement getStatement;
    private Statement statement;
    private ResultSet resultSet;
    private Connection con;

    public Statistics() {
        try {
            Class.forName(jdbcDriver);
            con = DriverManager.getConnection(dbAddress, userName, password);

            statement = con.createStatement();
            int myResult = statement.executeUpdate("CREATE DATABASE IF NOT EXISTS Statistics;");

            con.setAutoCommit(false);
            getStatement = con.prepareStatement("SELECT ? FROM Statistics.Stats WHERE Player=?");
            setStatement = con.prepareStatement("UPDATE Statistics.Stats SET ?=? WHERE Player=?");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Exception in Statistics class constructor");
        }

    }

    public void makeTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Stats" +
                "(id Integer not NULL AUTO_INCREMENT, " +
                "Player VARCHAR(255), " +
                "Kills Integer" +
                "Deaths Integer" +
                "Score Integer" +
                "PRIMARY KEY ( id ))";
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Exception while calling method makeTable in Statistics class");
        }
    }

    public void addPlayer(Player player) throws Exception {
        PreparedStatement statement = con.prepareStatement("INSERT INTO Statistics.Stats VALUES (?,?,?,?)");
        statement.setString(1, player.getNickname());
        statement.setInt(2, 0);
        statement.setInt(3, 0);
        statement.setInt(4, 0);
        int i = statement.executeUpdate();
        System.out.println(i + "records inserted");
    }

    public void addKill(Player player) throws Exception {
        int kills = this.getKills(player);
        kills++;
        setStatement.setString(1, "Kills");
        setStatement.setInt(2, kills);
        setStatement.setString(3, player.getNickname());
        setStatement.executeUpdate();
        con.commit();
        this.recalculateScore(player);
    }

    public void addDeath(Player player) throws Exception {
        int deaths = this.getDeaths(player);
        deaths++;
        setStatement.setString(1, "Deaths");
        setStatement.setInt(2, deaths);
        setStatement.setString(3, player.getNickname());
        setStatement.executeUpdate();
        con.commit();
        this.recalculateScore(player);
    }

    private void recalculateScore(Player player) throws Exception {
        int kills = this.getKills(player);
        int deaths = this.getDeaths(player);
        int score = (int) (kills / deaths * 0.3);
        score++;
        setStatement.setString(1, "Score");
        setStatement.setInt(2, score);
        setStatement.setString(3, player.getNickname());
        setStatement.executeUpdate();
        con.commit();
    }

    public String getNick(Player player) throws Exception {
        getStatement.setString(1, "Player");
        getStatement.setString(2, player.getNickname());
        resultSet = getStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString("Player");
        }
        System.out.println("No player found while trying to fetch one from the database, returning null...");
        return null;
    }

    public int getKills(Player player) throws Exception {
        getStatement.setString(1, "Kills");
        getStatement.setString(2, player.getNickname());
        resultSet = getStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getByte("Kills");
        }
        return 0;
    }

    public int getDeaths(Player player) throws Exception {
        getStatement.setString(1, "Deaths");
        getStatement.setString(2, player.getNickname());
        resultSet = getStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getByte("Deaths");
        }
        return 0;
    }

    public int getScore(Player player) throws Exception {
        getStatement.setString(1, "Score");
        getStatement.setString(2, player.getNickname());
        resultSet = getStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getByte("Score");
        }
        return 0;
    }

    public void closeConnection() {
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println("Exception while trying to close connection to the SQL server");
        }
        con = null;
    }

    public static void main(String[] args) {
        System.out.println("Testing");
        Statistics statistics = new Statistics();
        statistics.makeTable();
        System.out.println("success");
//        statistics.closeConnection();
    }

}
