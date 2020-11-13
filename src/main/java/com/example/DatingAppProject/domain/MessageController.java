package com.example.DatingAppProject.domain;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;

public class MessageController {
    private DataFacade facade = null;

    public MessageController(DataFacade facade) {
        this.facade = facade;
    }


    public ArrayList<User> getConversations(int id) throws DefaultException {
       // return facade.getConversations(id);
        return null;
    }

    public Model packageUser(User user, Model model) throws DefaultException {
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());
        return model;
    }

    /*
    public Model packageMessage(Message message, Model model) throws DefaultException {
        model.addAttribute("message", message.getMessage();
        return model;
    }*/
}
