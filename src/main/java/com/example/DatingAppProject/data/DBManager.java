package com.example.DatingAppProject.data;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBManager {
    private static String user;
    private static String password;
    private static String url;
    private static Connection connection = null;

    public static Connection getConnection(){
        if (connection != null) return connection;
        try (InputStream input = new FileInputStream("src/main/resources/application.properties")) {
            Properties properties = new Properties();
            properties.load(input);
            url = properties.getProperty("url");
            user = properties.getProperty("user");
            password = properties.getProperty("password");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(url,user,password);
            System.out.println("Succesfully connected to DB"); // REMOVE
            String setDB = "USE datingapp;";
            PreparedStatement ps = connection.prepareStatement(setDB);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
