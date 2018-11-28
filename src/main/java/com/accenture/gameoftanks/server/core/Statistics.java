package com.accenture.gameoftanks.server.core;

import java.sql.*;

public class Statistics {

    private String jdbcDriver = "com.mysql.jdbc.Driver";
    private String dbAddress = "jdbc:mysql://localhost:3306/Statistics?createDatabaseIfNotExists=true";
    private String userName = "root";
    private String password ="abcd";

    private PreparedStatement statement;
    private ResultSet result;
    private Connection con;

    public void run() {
        try {
            Class.forName(jdbcDriver);
            Connection conn = DriverManager.getConnection(dbAddress, userName, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void makeTable() throws Exception {

    }

}
