package com.accenture.gameoftanks.server.core;

import com.accenture.gameoftanks.core.Player;

import java.sql.*;

public class TemporaryDatabaseTable {

    private Connection con;
    private Statement statement;
    private PreparedStatement getStatement;
    private PreparedStatement setStatement;
    private ResultSet resultSet;

    public TemporaryDatabaseTable(Connection con, Statement statement) {
        this.con = con;
        this.statement = statement;

        try {
            getStatement = con.prepareStatement("SELECT nickname FROM GoT_Statistics.current_session WHERE nickname=?");
            setStatement = con.prepareStatement("UPDATE GoT_Statistics.current_session SET ?=? WHERE nickname=?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void makeTable() throws Exception {

        //TODO all values have to be copied over to all_sessions table before current_session table gets deleted. And for players whose name already exists in all sessions table the values have to be added using addition instead.

        String sessionTable = "CREATE TABLE IF NOT EXISTS current_session " +
                "(id INTEGER not NULL AUTO_INCREMENT," +
                " nickname VARCHAR(255)," +
                " kills INTEGER," +
                " deaths INTEGER," +
                " score INTEGER," +
                " PRIMARY KEY ( id ))";
        String dropSessionTable = "DROP TABLE IF EXISTS current_session";

        statement.execute(dropSessionTable);
        statement.execute(sessionTable);
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

    public boolean playerExists(Player player) {
        String nickname = player.getNickname();
        if (nickname.equals(getNickname(player))) {
            System.out.println("Player with a nickname " + nickname + " already exists.");
            return true;
        }
        return false;
    }

    public void addPlayer(Player player) {
        if (playerExists(player)) {
            return;
        }
        System.out.println("Adding player " + player.getNickname() + " to database.");
        try {
            PreparedStatement statement = con.prepareStatement("INSERT INTO GoT_Statistics.current_session (nickname, kills, deaths, score) VALUES (?, ?, ?, ?)");
            statement.setString(1, player.getNickname());
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
