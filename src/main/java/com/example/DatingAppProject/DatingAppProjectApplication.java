package com.example.DatingAppProject;

import com.example.DatingAppProject.data.DBManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DatingAppProjectApplication {

    public static void main(String[] args) {
        DBManager dbm = new DBManager();
        dbm.getConnection();

        SpringApplication.run(DatingAppProjectApplication.class, args);
    }

}
