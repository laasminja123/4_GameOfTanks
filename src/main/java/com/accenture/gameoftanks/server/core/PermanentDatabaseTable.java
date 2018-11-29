package com.accenture.gameoftanks.server.core;

import com.accenture.gameoftanks.core.Player;

import java.sql.*;

public class PermanentDatabaseTable {

    private Connection con;
    private Statement statement;
    private PreparedStatement getStatement;
    private PreparedStatement setStatement;
    private ResultSet resultSet;

    public PermanentDatabaseTable(Connection con, Statement statement) {
        this.con = con;
        this.statement = statement;

        try {
            getStatement = con.prepareStatement("SELECT nickname FROM GoT_Statistics.all_sessions WHERE nickname=?");
            setStatement = con.prepareStatement("UPDATE GoT_Statistics.all_sessions SET ?=? WHERE nickname=?");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void makeTable() throws Exception {
        String permanentTable = "CREATE TABLE IF NOT EXISTS all_sessions " +
                "(id INTEGER not NULL AUTO_INCREMENT," +
                " nickname VARCHAR(255)," +
                " kills INTEGER," +
                " deaths INTEGER," +
                " score INTEGER," +
                " PRIMARY KEY ( id ))";

        statement.execute(permanentTable);
        con.commit();
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

    public int getKills(Player player) {
        try {
            getStatement.setString(1, "kills");
            getStatement.setString(2, player.getNickname());
            resultSet = getStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getByte("kills");
            }
        } catch (SQLException e) {
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
            e.printStackTrace();
        }
        return 0;
    }

    public void addPlayer(Player player) {
        String nickname = player.getNickname();
        if (nickname.equals(getNickname(player))) {
            System.out.println("Player with a nickname " + nickname + " already exists.");
            return;
        }
        System.out.println("Adding player " + nickname + " to database.");
        try {
            PreparedStatement statement = con.prepareStatement("INSERT INTO GoT_Statistics.all_sessions (nickname, kills, deaths, score) VALUES (?, ?, ?, ?)");
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

}
