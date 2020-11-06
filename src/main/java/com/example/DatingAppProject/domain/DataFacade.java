package com.example.DatingAppProject.domain;

public interface DataFacade {

    public User login(String email, String password) throws DefaultException;

    public User createUser(User user) throws DefaultException;

}
