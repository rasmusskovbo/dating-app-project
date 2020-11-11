package com.example.DatingAppProject.data;

import com.example.DatingAppProject.domain.DefaultException;
import com.example.DatingAppProject.domain.User;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.transform.Result;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;

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
            String userInfoSQL = "INSERT INTO userinfo (idusers, phone, firstName, lastName, birthdate, gender) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement psUserinfo = con.prepareStatement(userInfoSQL);
            psUserinfo.setInt(1, user.getId());
            psUserinfo.setString(2, user.getPhone());
            psUserinfo.setString(3, user.getFirstName());
            psUserinfo.setString(4, user.getLastName());
            psUserinfo.setString(5, user.getBirthDate());
            psUserinfo.setString(6, user.getGender());
            psUserinfo.executeUpdate();

            //Insert into description table
            String describSQL = "INSERT INTO descriptions (idusers, aboutme) VALUES (?, ?)";
            PreparedStatement psDescrib = con.prepareStatement(describSQL);
            psDescrib.setInt(1, user.getId());
            psDescrib.setString(2, user.getAboutme());
            psDescrib.executeUpdate();

            //Check if hashtag exists in the system:
            String tagSQL = "SELECT * FROM hashtags;";
            PreparedStatement psTag = con.prepareStatement(tagSQL);
            ResultSet rsHashtags = psTag.executeQuery();
            boolean hasHashtag = false;
            int hashtagID = 0;
            while (rsHashtags.next()) {
                if (rsHashtags.getString("tag").equals(user.getTag())) {
                    hasHashtag = true;
                    hashtagID = rsHashtags.getInt("idhashtags");
                }
            }
            String insertTagSQL = "";
            if (!hasHashtag) { // If hashtag isnt present, create hashtag
                insertTagSQL = "INSERT INTO hashtags (tag) VALUE (?)";
                PreparedStatement psInsertTag = con.prepareStatement(insertTagSQL, Statement.RETURN_GENERATED_KEYS);
                psInsertTag.setString(1, user.getTag());
                psInsertTag.executeUpdate();
                ResultSet tagID = psInsertTag.getGeneratedKeys();
                tagID.next();
                hashtagID = tagID.getInt(1);
            }            // establish connection
            insertTagSQL = "INSERT INTO useshashtags (idusers, idhashtags) VALUE (?, ?)";
            PreparedStatement psAppendTag = con.prepareStatement(insertTagSQL);
            psAppendTag.setInt(1, id);
            psAppendTag.setInt(2, hashtagID);
            psAppendTag.executeUpdate();

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

    public ArrayList<User> getUsers() throws DefaultException { // Evt skal søge parametre ind her
        try {
            Connection con = DBManager.getConnection();
            String SQL = "SELECT * FROM users " +
                    "JOIN logininfo using (idusers)" +
                    "JOIN userinfo USING (idusers) " + //evt flere linjer for at trække billede med også. pt ingen billede
                    ";";
            PreparedStatement ps = con.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            ArrayList<User> users = new ArrayList<>();
            while (rs.next()) {
                User user = new User(
                        rs.getString("email"),
                        rs.getString("pword"),
                        rs.getString("role"),
                        rs.getString("phone"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("gender"),
                        rs.getString("birthDate")
                );
                users.add(user);
            }
            return users;
        }   catch (SQLException ex) {
            throw new DefaultException(ex.getMessage());
        }
    }

    public ArrayList<User> getUsers(int id) throws DefaultException { // Evt skal søge parametre ind her
        try {

            User sessionUser = getProfile(id);
            int sessionUserId = sessionUser.getId();

            Connection con = DBManager.getConnection();
            String SQL = "SELECT * FROM users " +
                    "JOIN userinfo USING (idusers) " +
                    "JOIN descriptions USING (idusers) " +
                    "JOIN useshashtags USING (idusers);"; //evt flere linjer for at trække billede med også. pt ingen billede
            PreparedStatement ps = con.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            ArrayList<User> users = new ArrayList<>();
            while (rs.next()) {
                int idUserDB = rs.getInt("idusers");
                User user = new User(
                        rs.getString("role"),
                        rs.getString("phone"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("gender"),
                        rs.getString("birthDate"),
                        rs.getString("aboutme"),
                        rs.getString("tag")
                );

                if (sessionUserId != idUserDB) {
                    users.add(user);
                }
            }
            return users;
        }   catch (SQLException ex) {
            throw new DefaultException(ex.getMessage());
        }
    }

    public User getProfile(int id) throws DefaultException {
        try {
            Connection con = DBManager.getConnection();
            String SQL = "SELECT * FROM users " +
                    "JOIN logininfo using (idusers) " +
                    "JOIN userinfo using (idusers) " +
                    "JOIN useshashtags using (idusers) " +
                    "JOIN descriptions using (idusers) " +
                    "JOIN hashtags using (idhashtags) " +
                    "WHERE idusers = ?";
            PreparedStatement ps = con.prepareStatement(SQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User(
                        rs.getString("email"),
                        rs.getString("pword"),
                        rs.getString("role"),
                        rs.getString("phone"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("gender"),
                        rs.getString("birthDate"),
                        rs.getString("aboutme"),
                        rs.getString("tag")
                );

                // Check for pictures separately:
                String SQLurl = "SELECT idusers, description from users join pictures using (idusers) where idusers = ?";
                PreparedStatement psDefaultPicture = con.prepareStatement(SQLurl);
                psDefaultPicture.setInt(1, id);
                ResultSet rsUserPictures= ps.executeQuery();

                rsUserPictures.next(); // skips to 2nd row as first row holds labels.
                if (rsUserPictures.next()) {
                    // insert user pictures here
                } else {
                    String SQLpic = "SELECT idpictures, description FROM pictures " +
                            "WHERE idpictures = 2;"; // Selects URL for standard picture
                    PreparedStatement psp = con.prepareStatement(SQLpic);
                    ResultSet rsURL = psp.executeQuery();
                    if (rsURL.next()) {
                        user.setProfilePictureURL(rsURL.getString("description")); // sets profilepicture to default.
                    } else {
                        throw new DefaultException("Default picture not found.");
                    }
                }
                user.setId(id);
                return user;
            } else {
                throw new DefaultException("Could not validate user");
            }
        }  catch (SQLException ex) {
            throw new DefaultException(ex.getMessage());
        }
    }

    public ArrayList<String> getTags() throws DefaultException {
        try {
            Connection con = DBManager.getConnection();
            String SQL = "SELECT tag FROM hashtags";
            PreparedStatement ps = con.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> tags = new ArrayList<>();
            while (rs.next()) {
                String tag = rs.getString("tag");
                if (!tags.contains(tag)) {
                    tags.add(tag);
                }
            }
            return tags;
        } catch (SQLException ex) {
            throw new DefaultException(ex.getMessage());
        }
    }

    public void uploadPicture(MultipartFile multipartFile) throws SQLException, IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        InputStream inputStream = multipartFile.getInputStream();

        Connection con = DBManager.getConnection();
        String SQL = "INSERT INTO pictures (description, data) VALUES (?, ?)";
        PreparedStatement ps = con.prepareStatement(SQL);
        ps.setString(1, fileName);
        ps.setBlob(2, inputStream);
        ps.executeQuery(); //exeucute update
    }

    // Virker ikke
    public Blob getPicture(int id) throws SQLException, IOException {
        Connection con = DBManager.getConnection();
        MultipartFile multipartFile = null;
        Blob blob = null;

        String SQL = "SELECT * FROM pictures WHERE idpictures = ?";
        PreparedStatement ps = con.prepareStatement(SQL);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            blob = rs.getBlob("data");
        }
        return blob;
    }


    // test
    private User getUser(ResultSet rs) throws SQLException {
        User user = new User(
                rs.getString("email"),
                rs.getString("pword"), //bør måske ikke være med her
                rs.getString("role"),
                rs.getString("phone"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("gender"),
                rs.getString("birthDate")
        );
        return user;
    }
}
