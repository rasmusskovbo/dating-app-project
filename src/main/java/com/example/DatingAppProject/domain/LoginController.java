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

        public User createUser(String email, String password) throws DefaultException {
            // By default, new users are customers
            User user = new User(email, password, "Costumer");
            facade.createUser(user);
            return user;
        }
}



