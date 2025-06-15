package com.vdmytriv.bookstoreapp;

import com.vdmytriv.bookstoreapp.model.Book;
import com.vdmytriv.bookstoreapp.service.BookService;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookstoreAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookstoreAppApplication.class, args);
    }

    @Bean
    public CommandLineRunner testBooks(BookService bookService) {
        return args -> {
            Book book = new Book();
            book.setTitle("Clean Code");
            book.setAuthor("Robert C. Martin");
            book.setIsbn("9780132350884");
            book.setPrice(new BigDecimal("45.99"));
            book.setDescription("A handbook of agile software craftsmanship.");
            book.setCoverImage("cleancode.jpg");

            bookService.save(book);

            System.out.println("📚 All books:");
            bookService.findAll().forEach(System.out::println);
        };
    }
}
