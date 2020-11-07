package com.example.DatingAppProject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//TEST IMPORTS ONLY
import java.sql.*;
import com.example.DatingAppProject.data.DBManager;

@SpringBootApplication
public class DatingAppProjectApplication {

    public static void main(String[] args) {

        SpringApplication.run(DatingAppProjectApplication.class, args);

    }
}
