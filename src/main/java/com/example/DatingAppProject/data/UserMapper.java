package com.example.DatingAppProject.data;

import com.example.DatingAppProject.domain.DefaultException;
import com.example.DatingAppProject.domain.User;

import java.sql.*;

// Snakker med databasen, ift user queries
public class UserMapper {

    public void createUser(User user) throws DefaultException {
        try {
            Connection con = DBManager.getConnection();
            String SQL = "INSERT INTO users (email, role) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getRole());
            // ps.setString(2, user.getPassword());
            ps.executeUpdate();
            ResultSet ids = ps.getGeneratedKeys();
            ids.next();
            int id = ids.getInt(1);
            user.setId(id);
        } catch (SQLException ex) {
            throw new DefaultException(ex.getMessage());
        }
    }

    public User login(String email, String password) throws DefaultException {
        try {
            Connection con = DBManager.getConnection();
            String SQL = "SELECT id, role FROM Users "
                    + "WHERE email=? AND password=?";
            PreparedStatement ps = con.prepareStatement(SQL);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                int id = rs.getInt("id");
                User user = new User(email, password, role);
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
