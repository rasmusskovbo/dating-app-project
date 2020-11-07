package com.example.DatingAppProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//TEST IMPORTS ONLY
import java.sql.*;
import com.example.DatingAppProject.data.DBManager;

@SpringBootApplication
public class DatingAppProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatingAppProjectApplication.class, args);

        /* Testing working connection
        try {
            DBManager dbm = new DBManager();


            Connection con = dbm.getConnection();
            String SQL = "INSERT into defaultdb.users (email, role) VALUES (?, ?);";
            PreparedStatement ps = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, "Rasmus");
            ps.setString(2, "User");
            ps.executeUpdate();
            ResultSet ids = ps.getGeneratedKeys();
            ids.next();
            int id = ids.getInt(1);
            System.out.println("Succesfully inserted and got key: " + id);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        */
    }

}
