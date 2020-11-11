package com.example.DatingAppProject.controller;

import com.example.DatingAppProject.data.DataFacadeImpl;
import com.example.DatingAppProject.domain.DefaultException;
import com.example.DatingAppProject.domain.LoginController;
import com.example.DatingAppProject.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

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
    public String deletUser() {
        return "userpages/deleteUser";
    }

    @GetMapping("/test")
    public String tester() {
        return "test";
    }

    @PostMapping("/userDeleted")
    public String userDeleted(WebRequest request, Model model) throws DefaultException {
            //Retrieve values from HTML form via WebRequest
            String email = request.getParameter("email");
            String firstName = request.getParameter("firstName");

            User user = loginController.login(email, firstName); // UserMapper checks with Database for user.
            setSessionInfo(request, user);

            if (user.getRole().equals("user")) {
                return "redirect:/userDeleted";
            } else {
                return "exceptionPage";
            }
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

    @PostMapping("/register")
    public String createUser(WebRequest request, Model model) throws DefaultException {
        //Retrieve values from HTML form via WebRequest
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");
        String birthDate = request.getParameter("birthDate");
        String email = request.getParameter("email");
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

    @PostMapping("/testUpload")
    public String getPicture(@RequestParam("file")MultipartFile multipartFile) throws SQLException, IOException {
        loginController.uploadPicture(multipartFile);
        return "welcome";
    }

    @GetMapping("/testshow")
    public String showPicture(Model model) throws IOException, SQLException {
        Blob blob = loginController.getPicture();
        return "testshow";
    }

    private void setSessionInfo(WebRequest request, User user) {
        // Place user info on session
        request.setAttribute("user", user, WebRequest.SCOPE_SESSION);
        request.setAttribute("role", user.getRole(), WebRequest.SCOPE_SESSION);
        request.setAttribute("id", user.getId(), WebRequest.SCOPE_SESSION);
    }


}

