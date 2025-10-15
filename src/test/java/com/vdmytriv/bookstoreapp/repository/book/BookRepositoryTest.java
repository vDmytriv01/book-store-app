package com.vdmytriv.bookstoreapp.repository.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.vdmytriv.bookstoreapp.config.CustomMySqlContainer;
import com.vdmytriv.bookstoreapp.model.Book;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Container
    private static final CustomMySqlContainer container = CustomMySqlContainer.getInstance();

    @Autowired
    private BookRepository bookRepository;

    @Test
    @Sql(scripts = {
            "classpath:database/category/add-test-category.sql",
            "classpath:database/book/add-test-book.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book/remove-test-book.sql",
            "classpath:database/category/remove-test-category.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategories_Id_ReturnsBookWithAllFields() {

        Page<Book> result = bookRepository.findAllByCategories_Id(1L, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements(), "Expected one book in category 1");
        Book book = result.getContent().get(0);
        assertNotNull(book);

        assertEquals(1L, book.getId());
        assertEquals("Effective Java", book.getTitle());
        assertEquals("Joshua Bloch", book.getAuthor());
        assertEquals("978-3-16-148410-1", book.getIsbn());
        assertEquals(BigDecimal.valueOf(49.99), book.getPrice());
        assertEquals("A book about Java best practices.", book.getDescription());
        assertEquals("https://example.com/image.jpg", book.getCoverImage());
    }
}
