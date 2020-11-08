package com.example.DatingAppProject.controller;

import com.example.DatingAppProject.data.DataFacadeImpl;
import com.example.DatingAppProject.domain.DefaultException;
import com.example.DatingAppProject.domain.LoginController;
import com.example.DatingAppProject.domain.User;
import org.springframework.stereotype.Controller;
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

    @GetMapping("/message")
    public String message() {
        return "message";
    }

   @GetMapping("/homepage")
   public String getHomepage(WebRequest request) {
    // Retrieve user object from web request (session scope)
    User user = (User) request.getAttribute("user",WebRequest.SCOPE_SESSION);

    // If user object is found on session, i.e. user is logged in, she/he can see secretstuff page
    if (user != null) {
        return "userpages/homepage";
    }
    else
        return "redirect:/createProfile";
}

/*    @PostMapping("/login")
    public String loginUser(WebRequest request) throws DefaultException {
        //Retrieve values from HTML form via WebRequest
        String email = request.getParameter("email");
        String pwd = request.getParameter("password");

        // delegate work + data to login controller
        User user = loginController.login(email, pwd);
        setSessionInfo(request, user);

        // Go to to page dependent on role
        return "userpages/" + user.getRole();
    }
*/
    @PostMapping("/register")
    public String createUser(WebRequest request) throws DefaultException {
        //Retrieve values from HTML form via WebRequest
        String name = request.getParameter("name");
        String lastName = request.getParameter("lastName");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");
        String birthDate = request.getParameter("birthDate");
        String email = request.getParameter("email");
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");

        // If passwords match, work + data is delegated to logic controller
        //vi skal have oprettet en user med alle varibaler og evt. chekke format for variabler.
        /*
        if (password1.equals(password2)) {
            User user = loginController.createUser(email, password);
            setSessionInfo(request, user);

            // Go to page dependent on role
            return "userpages/" + user.getRole();

        } else { // If passwords don't match, an exception is thrown
            throw new DefaultException("The two passwords did not match");
        }
        */
        return null;
    }

    private void setSessionInfo(WebRequest request, User user) {
        // Place user info on session
        request.setAttribute("user", user, WebRequest.SCOPE_SESSION);
        request.setAttribute("role", user.getRole(), WebRequest.SCOPE_SESSION);
    }

}


