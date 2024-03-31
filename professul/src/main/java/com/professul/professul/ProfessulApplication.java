package com.professul.professul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProfessulApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProfessulApplication.class, args);
    }

}
