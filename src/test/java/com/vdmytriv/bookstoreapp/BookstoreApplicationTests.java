package com.vdmytriv.bookstoreapp;

import com.vdmytriv.bookstoreapp.config.CustomMySqlContainer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class BookstoreApplicationTests {
    @Container
    private static final CustomMySqlContainer container = CustomMySqlContainer.getInstance();

    @Test
    void contextLoads() {
    }
}
