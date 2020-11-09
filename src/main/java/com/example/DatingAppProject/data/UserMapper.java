package com.example.DatingAppProject.data;

import com.example.DatingAppProject.domain.DefaultException;
import com.example.DatingAppProject.domain.User;

import java.sql.*;

// Snakker med databasen, ift user queries
public class UserMapper {

    public void createUser(User user) throws DefaultException {
        try {
            Connection con = DBManager.getConnection();

            // Insert into users table
            String SQL = "INSERT INTO users (email, role) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getRole());
            ps.executeUpdate();
            ResultSet ids = ps.getGeneratedKeys();
            ids.next();
            int id = ids.getInt(1);
            user.setId(id);

            // Insert into password table
            String loginInfoSQL = "INSERT INTO logininfo (idusers, pword) VALUES (?, ?)";
            PreparedStatement psLogininfo = con.prepareStatement(loginInfoSQL);
            psLogininfo.setInt(1, user.getId());
            psLogininfo.setString(2, user.getPassword());
            psLogininfo.executeUpdate();

            // Insert into userinfo table
            String userInfoSQL = "INSERT INTO userinfo (idusers, phone, firstname, lastname, birthdate, gender) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement psUserinfo = con.prepareStatement(userInfoSQL);
            psUserinfo.setInt(1, user.getId());
            psUserinfo.setString(2, user.getPhone());
            psUserinfo.setString(3, user.getFirstName());
            psUserinfo.setString(4, user.getLastName());
            psUserinfo.setString(5, user.getBirthDate());
            psUserinfo.setString(6, user.getGender());
            psUserinfo.executeUpdate();

        } catch (SQLException ex) {
            throw new DefaultException(ex.getMessage()); // TODO Fejlmeddelelse skal returneres til site via model og thymeleaf
        }
    }

    // Loads USER ID into Session object.
    public User login(String email, String password) throws DefaultException {
        try {
            Connection con = DBManager.getConnection();
            String SQL = "SELECT * from users "
                    + "JOIN logininfo using (idusers) "
                    + "WHERE email=? AND pword=?";
            PreparedStatement ps = con.prepareStatement(SQL);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                int id = rs.getInt("idusers");
                User user = new User(email, password);
                user.setId(id);
                return user;
            } else {
                throw new DefaultException("Could not validate user");
            }
        } catch (SQLException ex) {
            throw new DefaultException(ex.getMessage());
        }
    }

}
