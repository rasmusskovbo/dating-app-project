package com.example.DatingAppProject.domain;

import com.example.DatingAppProject.data.DBManager;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginController {
        // facade to datasource layer
        private DataFacade facade = null;

        public LoginController(DataFacade facade) {
            this.facade = facade;
        }

        public User login(String email, String password) throws DefaultException {
            return facade.login(email, password);
        }

        public User createUser(String email, String password, String role, String phone, String firstName, String lastName, String gender, String birthDate, String aboutme, String tag) throws DefaultException {
            // By default, new users role are "user"
            User user = new User(email, password, role, phone, firstName, lastName, gender, birthDate, aboutme, tag);
            facade.createUser(user); // creates user in MYSQL
            return user;
        }

        public User getProfile(int id) throws DefaultException {
            return facade.getProfile(id);
        }

        public void editProfile(User user) throws DefaultException {
            facade.editProfile(user);
        }

        public Model packageUser(User user, Model model) throws DefaultException {
            model.addAttribute("email", user.getEmail());
            model.addAttribute("firstName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
            model.addAttribute("phone", user.getPhone());
            model.addAttribute("gender", user.getGender());
            model.addAttribute("birthDate", user.getBirthDate()); // evt lave udregning af alder her inden pakning
            model.addAttribute("profilePictureURL", user.getProfilePictureURL());
            System.out.println(user.getAboutme());
            model.addAttribute("aboutme", user.getAboutme());
            model.addAttribute("userslist", user.getId()); //Get list of all users to show on homepage table list
            model.addAttribute("tags", getTags()); //Get list of all tags being used to show on search drop down list
            return model;
        }

        public Model getUsers(Model model) throws DefaultException {
            model.addAttribute("users", facade.getUsers());
            return model;
        }


        // test
        public void uploadPicture(MultipartFile multipartFile) throws SQLException, IOException {
            facade.uploadPicture(multipartFile);
        }

        public Blob getPicture() throws SQLException, IOException {
            return facade.getPicture(3);
        }

    public ArrayList<User> getUsers(int id) throws DefaultException {
        return facade.getUsers(id);
    }


    public ArrayList<String> getTags() throws DefaultException{
        return facade.getTags();
    }



}



