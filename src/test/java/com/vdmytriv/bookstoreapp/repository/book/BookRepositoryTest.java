package com.vdmytriv.bookstoreapp.repository.book;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.vdmytriv.bookstoreapp.config.CustomMySqlContainer;
import com.vdmytriv.bookstoreapp.model.Book;
import com.vdmytriv.bookstoreapp.util.TestDataFactory;
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
        assertTrue(result.hasContent(), "Result should contain books");

        Book actual = result.getContent().get(0);
        assertNotNull(actual, "Book should not be null");

        Book expected = TestDataFactory.createBook();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id", "categories")
                .isEqualTo(expected);
    }
}
