package com.example.DatingAppProject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//TEST IMPORTS ONLY
import java.sql.*;
import com.example.DatingAppProject.data.DBManager;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class DatingAppProjectApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {

        SpringApplication.run(DatingAppProjectApplication.class, args);

    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(DatingAppProjectApplication.class);
    }
}
