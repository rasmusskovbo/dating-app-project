package com.example.DatingAppProject.controller;

import com.example.DatingAppProject.controller.input_validation.RegistrationData;
import com.example.DatingAppProject.controller.input_validation.RegistrationResponse;
import com.example.DatingAppProject.controller.input_validation.ValidationController;
import com.example.DatingAppProject.data.DataFacadeImpl;
import com.example.DatingAppProject.domain.DefaultException;
import com.example.DatingAppProject.domain.LoginController;
import com.example.DatingAppProject.domain.User;
import org.springframework.boot.Banner;
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
import java.util.ArrayList;

@Controller
public class WebController {
    //use case controller (GRASP Controller) - injects concrete facade instance into controller
    private LoginController loginController = new LoginController(new DataFacadeImpl());
    private ValidationController validationController = new ValidationController();

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

    @GetMapping("/favorites")
    public String viewFavorites(WebRequest request, Model model) throws DefaultException {
        // Gets active user and packages
        int id = (int) request.getAttribute("id", WebRequest.SCOPE_SESSION);
        User user = loginController.getProfile(id); // Gets ID from session object, uses it to fetch profile.
        loginController.packageUser(user, model);

        // Packages all other users except for active one.
        model.addAttribute("userlist", loginController.getUsers("", id, "favorites")); //lists all users with chosen tag excluding active user

        return "userpages/favorites";
    }

    @GetMapping("/admin")
    public String admin(WebRequest request, Model model) throws DefaultException {
        // Gets admin id and shows all other users except admin
        int id = (int) request.getAttribute("id", WebRequest.SCOPE_SESSION);
        model.addAttribute("userlist", loginController.getUsers("", id, "admin"));

        return "userpages/admin";
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
        String aboutme = request.getParameter("aboutme");
        String tag = request.getParameter("tag");
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");

        // Create class with data
        RegistrationData regData = new RegistrationData(firstName, lastName, email, phone, gender, birthDate, aboutme, tag, password1, password2);
        // Puts data into controller
        validationController.setRegData(regData);
        // Validates data and returns response set
        RegistrationResponse response = validationController.validate();
        // Checks if all inputs are validated. If not, packages error message from response and refreshes site.

        if (!response.isInputOK()) {
            model.addAttribute("errorMsg", response.getError());
            return "createProfile";
        } else {
            if (password1.equals(password2)) {
                User user = loginController.createUser(email, password1, "user", phone, firstName, lastName, gender, birthDate, aboutme, tag);
                setSessionInfo(request, user);
                return "redirect:/profile";
            } else { // If passwords don't match, an exception is thrown
                throw new DefaultException("The two passwords did not match");
            }
        }
    }


    @PostMapping("/loginAction")
    public String loginUser(WebRequest request) throws DefaultException {
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

    @GetMapping("/profile")
    public String getProfile(WebRequest request, Model model) throws DefaultException {
        //Being used to remove session user from homepage table list
        int id = (int) request.getAttribute("id", WebRequest.SCOPE_SESSION);

        User user = loginController.getProfile(id); // Gets ID from session object, uses it to fetch profile.
        loginController.packageUser(user, model);

        // Packages all other users except for active one.
        model.addAttribute("userlist", loginController.getUsers("", user.getId(), "profile"));

        return "userpages/profile";
    }

    @PostMapping("searchUsers")
    public String searchUsers(WebRequest request, Model model) throws DefaultException {
        String searchTag = request.getParameter("searchTag");

        // Gets active user and packages
        int id = (int) request.getAttribute("id", WebRequest.SCOPE_SESSION);
        User user = loginController.getProfile(id); // Gets ID from session object, uses it to fetch profile.
        loginController.packageUser(user, model);

        // Packages all other users except for active one.
        model.addAttribute("userlist", loginController.getUsers(searchTag, id, "searchTag")); //lists all users with chosen tag excluding active user

        return "userpages/profile";
    }

    @PostMapping("/addFavorite")
    public String addFavorite(WebRequest request, Model model) throws DefaultException {
        int id = (int) request.getAttribute("id", WebRequest.SCOPE_SESSION);
        int favorite = Integer.parseInt(request.getParameter("favorite"));
        loginController.addFavorite(id, favorite);

        return "redirect:/favorites";
    }
    @PostMapping("removeFavorite")
    public String removeFavorites(WebRequest request) throws DefaultException{
        String removeUserId = request.getParameter("favorite");
        loginController.removeFavorite(removeUserId);

        return "redirect:/favorites";
    }

    @PostMapping("editProfile")
    public String editProfile(WebRequest request, Model model) throws DefaultException {
        //Retrieve values from HTML form via WebRequest
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

        // Create class with data
        RegistrationData regData = new RegistrationData(firstName, lastName, email, phone, gender, birthDate, aboutme, tag, password1, password2);
        // Puts data into controller
        validationController.setRegData(regData);
        // Validates data and returns response set
        RegistrationResponse response = validationController.validate();
        // Checks if all inputs are validated. If not, packages error message from response and refreshes site.

        if (!response.isInputOK()) {
            model.addAttribute("errorMsg", response.getError());
            return "userpages/profile#popup"; // TODO FIX LINK
        }

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

    @PostMapping("removeUsers")
    public String removeUsers(WebRequest request) throws DefaultException {
        String removeUserId = request.getParameter("userId");
        loginController.removeUser(removeUserId);

        return "redirect:/admin";
    }

    @PostMapping("/testUpload")
    public String getPicture(@RequestParam("file") MultipartFile multipartFile) throws SQLException, IOException {
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

    @ExceptionHandler(Exception.class)
    public String anotherError(Model model, Exception exception) {
        model.addAttribute("message",exception.getMessage());
        return "exceptionPage";
    }

}

