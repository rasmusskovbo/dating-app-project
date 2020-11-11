package com.example.DatingAppProject.domain;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;

public interface DataFacade {

    public User login(String email, String password) throws DefaultException;

    public User createUser(User user) throws DefaultException;

    public User getProfile(int id) throws DefaultException;

    public void editProfile(User user) throws DefaultException;

    public ArrayList<User> getUsers() throws DefaultException;

    public ArrayList<User> getUsers(int id) throws DefaultException;

    public void uploadPicture(MultipartFile multipartFile) throws SQLException, IOException;

    public Blob getPicture(int id) throws SQLException, IOException;

    public ArrayList<String> getTags() throws DefaultException;

}
