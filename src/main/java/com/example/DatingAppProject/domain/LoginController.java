package com.example.DatingAppProject.domain;

public class LoginController {
        // facade to datasource layer
        private DataFacade facade = null;

        public LoginController(DataFacade facade) {
            this.facade = facade;
        }

        public User login(String email, String password, String role) throws DefaultException {
            return facade.login(email, password);
        }

        public User createUser(String email, String password, String role, String phone, String firstName, String lastName, String gender, String birthDate) throws DefaultException {
            // By default, new users role are "user"
            User user = new User(email, password, role, phone, firstName, lastName, gender, birthDate);
            System.out.println("USER BEFORE FACADE:");
            System.out.println(user.toString());
            facade.createUser(user); // creates user in MYSQL
            return user;
        }
}



