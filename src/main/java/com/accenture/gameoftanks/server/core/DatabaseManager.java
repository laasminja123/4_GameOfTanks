package com.accenture.gameoftanks.server.core;

import com.accenture.gameoftanks.core.Player;

import java.sql.*;

public class DatabaseManager {

    private String dbAddress = "jdbc:mysql://localhost/?useSSL=false";
//    private String userName = "root";
//    private String password = "abcd1234";
    private Connection con;
    private Statement statement;
    private PermanentDatabaseTable permanentTable;
    private TemporaryDatabaseTable temporaryTable;

    public DatabaseManager(String username, String password) {
        try {
            con = DriverManager.getConnection(dbAddress, username, password);
            statement = con.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS GoT_Statistics;");
            con.setCatalog("GoT_Statistics");
            statement = con.createStatement();
            con.setAutoCommit(false);

            permanentTable = new PermanentDatabaseTable(con, statement);
            temporaryTable = new TemporaryDatabaseTable(con, statement);

        } catch (SQLException e) {
            System.out.println("Exception in DatabaseManager class constructor");
        }
    }

    public void createTables() {
        try {
            permanentTable.makeTable();
            temporaryTable.makeTable();
        } catch (Exception e) {
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

    public boolean playerExists(Player player) {
        return temporaryTable.playerExists(player);
    }

    public void addPlayer(Player player) {
        temporaryTable.addPlayer(player);
    }

}