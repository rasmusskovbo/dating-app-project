package com.example.DatingAppProject.data;

import com.example.DatingAppProject.domain.DefaultException;
import com.example.DatingAppProject.domain.User;

import java.sql.*;

// Snakker med databasen, ift user queries
public class UserMapper {

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
