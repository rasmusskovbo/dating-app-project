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
    public String admin(Model model) throws DefaultException {
        loginController.getUsers(model);
        return "userpages/admin";
    }

    @GetMapping("/deleteUser")
    public String deletUser() {
        return "userpages/deleteUser";
    }

    @GetMapping("/userDeleted")
    public String userDeleted() {
        return "userpages/userDeleted";
    }

    @GetMapping("/test")
    public String tester() {
        return "test";
    }

    /*
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

 */

    @PostMapping("searchUsers")
    public String searchUsers(WebRequest request) throws DefaultException {
        String searchTag = request.getParameter("searchTag");
        // TODO Reload profile og kun returnere arraylist med users der matcher tags.
        return "";
    }

    @PostMapping("editProfile")
    public String editProfile(WebRequest request) throws DefaultException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");
        String birthDate = request.getParameter("birthDate");
        String aboutme = request.getParameter("aboutme");
        String tag = request.getParameter("tag");
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");
        User user = null;
        if (password1.equals(password2)) {
            user = new User(email, password1, "user", phone, firstName, lastName, gender, birthDate, aboutme, tag);
            int id = (int) request.getAttribute("id", WebRequest.SCOPE_SESSION);
            user.setId(id);
            System.out.println(user.toString());
            loginController.editProfile(user);

            return "redirect:/profile";
        } else {
            throw new DefaultException("Passwords not the same");
        }
    }

    @GetMapping("/profile")
    public String getProfile(WebRequest request, Model model) throws DefaultException {
        //Being used to remove session user from homepage table list
        int id = (int) request.getAttribute("id", WebRequest.SCOPE_SESSION);

        User user = loginController.getProfile(id); // Gets ID from session object, uses it to fetch profile.
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
        String email = request.getParameter("email");
        boolean isCorrect = loginController.stringValidation(email, "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
        if (!isCorrect) {
            model.addAttribute("errorMsg", "Please enter a valid e-mail");
            isCorrect = false;
            return "createProfile";
        }
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");
        String birthDate = request.getParameter("birthDate");
        String aboutme = request.getParameter("aboutme");
        String tag = request.getParameter("tag");
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");

        if (password1.equals(password2)) {
            User user = loginController.createUser(email, password1, "user", phone, firstName, lastName, gender, birthDate, aboutme, tag);
            setSessionInfo(request, user);
            return "redirect:/profile";
        } else { // If passwords don't match, an exception is thrown
            throw new DefaultException("The two passwords did not match");
        }
    }

    // Exception handler

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

