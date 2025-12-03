package com.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookstoreApplication {
    public static void main(String[] args) {
        System.out.println("=== STARTING BOOKSTORE APP ===");
        try {
            SpringApplication.run(BookstoreApplication.class, args);
            System.out.println("=== APP STARTED SUCCESSFULLY ===");
        } catch (Exception e) {
            System.err.println("=== APP FAILED TO START ===");
            e.printStackTrace();
            System.exit(1);
        }
    }
}