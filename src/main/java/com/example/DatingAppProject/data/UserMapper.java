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
            String loginInfoSQL = "INSERT INTO logininfo (idusers, password) VALUES (?, ?)";
            PreparedStatement psLogininfo = con.prepareStatement(loginInfoSQL);
            psLogininfo.setInt(1, user.getId());
            //psLogininfo.setString(2, user.getPassword()); // TODO Implement password on user class
            psLogininfo.executeUpdate();

            // Insert into userinfo table
            String userInfoSQL = "INSERT INTO userinfo (idusers, phone, firstname, lastname, birthdate, gender) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement psUserinfo = con.prepareStatement(userInfoSQL);
            psUserinfo.setInt(1, user.getId());
<<<<<<< HEAD
            /*psUserinfo.setInt(2, user.getPhone());
            psUserinfo.setString(3, user.getFirstName());
            psUserinfo.setString(4, user.getLastName());
            psUserinfo.setDate(5, user.getBirthdate());
            psUserinfo.setString(6, user.getGender());*/
=======
            psUserinfo.setInt(2, user.getPhone());
            psUserinfo.setString(3, user.getFirstName());
            psUserinfo.setString(4, user.getLastName());
            psUserinfo.setDate(5, user.getBirthDate());
            psUserinfo.setString(6, user.getGender());
>>>>>>> d1e473e3743545b1fd28c6874c7a1f2be3f0c983
            psUserinfo.executeUpdate();

        } catch (SQLException ex) {
            throw new DefaultException(ex.getMessage()); // TODO Fejlmeddelelse skal returneres til site via model og thymeleaf
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
