package edu.leti.documentsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class DocumentSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(DocumentSystemApplication.class, args);
    }
}
