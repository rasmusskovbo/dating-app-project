package com.example.DatingAppProject.controller;

import com.example.DatingAppProject.data.DataFacadeImpl;
import com.example.DatingAppProject.domain.DefaultException;
import com.example.DatingAppProject.domain.LoginController;
import com.example.DatingAppProject.domain.User;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
public class WebController {
    //use case controller (GRASP Controller) - injects concrete facade instance into controller
    private LoginController loginController = new LoginController(new DataFacadeImpl());

    @GetMapping("/")
    public String login() {
        return "welcome";
    }

    @GetMapping("/createProfile")
    public String createProfile() {
        return "createProfile";
    }

    @GetMapping("/logIn")
    public String logIn() {
        return "login";
    }

    @GetMapping("/messages")
    public String messages() {
        return "userpages/messages";
    }

    @GetMapping("/admin")
    public String admin() {
        return "userpages/admin";
    }
    @GetMapping("/deleteUser")
    public String deleteData() {
        return "userpages/deleteUser";
    }
    @GetMapping("/profile")
    public String getProfile(WebRequest request, Model model) throws DefaultException {
        User user = loginController.getProfile((int) request.getAttribute("id", WebRequest.SCOPE_SESSION)); // Gets ID from session object, uses it to fetch profile.
        loginController.packageUser(user, model);
        return "userpages/profile";
    }

    @PostMapping("/loginAction")
    public String loginUser(WebRequest request, Model model) throws DefaultException {
        //Retrieve values from HTML form via WebRequest
        String email = request.getParameter("email");
        String pwd = request.getParameter("password");

        User user = loginController.login(email, pwd); // UserMapper checks with Database for user.
        setSessionInfo(request, user);

        if (user.getRole().equals("user")) {
            return "redirect:/profile";
        } else if (user.getRole().equals("admin")) {
            return "redirect:/admin";
        } else {
            return "exceptionPage";
        }
    }


    @GetMapping("/test")
    public String testSite() {
        return "test";
    }


    @PostMapping("/register")
    public String createUser(WebRequest request, Model model) throws DefaultException {
        //Retrieve values from HTML form via WebRequest
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");
        String birthDate = request.getParameter("birthDate");
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");

        if (password1.equals(password2)) {
            User user = loginController.createUser(email, password1, "user", phone, firstName, lastName, gender, birthDate);
            setSessionInfo(request, user);
            return "redirect:/profile";
        } else { // If passwords don't match, an exception is thrown
            throw new DefaultException("The two passwords did not match");
        }
    }

    private void setSessionInfo(WebRequest request, User user) {
        // Place user info on session
        request.setAttribute("user", user, WebRequest.SCOPE_SESSION);
        request.setAttribute("role", user.getRole(), WebRequest.SCOPE_SESSION);
        request.setAttribute("id", user.getId(), WebRequest.SCOPE_SESSION);
    }

}

