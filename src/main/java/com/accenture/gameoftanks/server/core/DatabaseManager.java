package com.accenture.gameoftanks.server.core;

import com.accenture.gameoftanks.core.Player;

import java.sql.*;

public class DatabaseManager extends Thread {

    private String dbAddress = "jdbc:mysql://localhost/?useSSL=false";
    private String userName = "root";
    private String password = "abcd1234";
    private Connection con;
    private Statement statement;
    private PermanentDatabaseTable permanentTable;
    private TemporaryDatabaseTable temporaryTable;

    //TODO add userName and password to the constructor so that the user is required to type them

    public DatabaseManager() {
        try {
            con = DriverManager.getConnection(dbAddress, userName, password);
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

    @Override
    public void run() {
        super.run();
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