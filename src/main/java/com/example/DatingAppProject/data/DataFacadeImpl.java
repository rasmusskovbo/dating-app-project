package com.example.DatingAppProject.data;

import com.example.DatingAppProject.domain.DefaultException;
import com.example.DatingAppProject.domain.DataFacade;
import com.example.DatingAppProject.domain.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataFacadeImpl implements DataFacade {
    private UserMapper userMapper = new UserMapper();


    public User login(String email, String password) throws DefaultException {
        return userMapper.login(email, password);
    }


    public User createUser(User user) throws DefaultException  {
        System.out.println("USER BEFORE SQL:");
        System.out.println(user.toString());
        userMapper.createUser(user);
        return user;
    }

    public User getProfile(int id)  throws DefaultException {
        return userMapper.getProfile(id);
    }

    public void uploadPicture(MultipartFile multipartFile) throws SQLException, IOException {
        userMapper.uploadPicture(multipartFile);
    }

    public Blob getPicture(int id) throws SQLException, IOException {
        return userMapper.getPicture(id);
    }

    public ArrayList<User> getUsers() throws DefaultException {
        return userMapper.getUsers();
    }


}
