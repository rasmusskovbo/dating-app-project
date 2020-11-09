package com.example.DatingAppProject.data;

import com.example.DatingAppProject.domain.DefaultException;
import com.example.DatingAppProject.domain.DataFacade;
import com.example.DatingAppProject.domain.User;

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
}
