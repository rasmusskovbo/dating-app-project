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
        userMapper.createUser(user);
        return user;
    }

    public User getProfile(int id)  throws DefaultException {
        return userMapper.getProfile(id);
    }

    public void addFavorite(int id, int favorite) throws DefaultException {
        userMapper.addFavorite(id, favorite);
    }

    public void uploadPicture(MultipartFile multipartFile) throws SQLException, IOException {
        userMapper.uploadPicture(multipartFile);
    }

    public ArrayList<User> getUsers(String searchTag, int id, String segment) throws DefaultException {
        return userMapper.getUsers(searchTag, id, segment);
    }

    public ArrayList<String> getTags() throws DefaultException{
        return userMapper.getTags();
    }

    public void editProfile(User user) throws DefaultException {
        userMapper.editProfile(user);
    }

    public void removeUser(String removeUserId) throws DefaultException{
        userMapper.removeUser(removeUserId);
    }

    public void removeFavorite(String removeUserId) throws DefaultException {
        userMapper.removeFavorite(removeUserId);
    }


}
