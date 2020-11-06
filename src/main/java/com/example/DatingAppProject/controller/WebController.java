package com.example.DatingAppProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

        @GetMapping("/")
        public String login() {
            return "welcome";
        }

        @GetMapping("/homepage")
        public String homepage(){
            return "homepage";
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
    }


