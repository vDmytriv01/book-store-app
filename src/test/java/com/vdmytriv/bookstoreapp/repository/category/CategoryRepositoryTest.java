package com.vdmytriv.bookstoreapp.repository.category;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.vdmytriv.bookstoreapp.config.CustomMySqlContainer;
import com.vdmytriv.bookstoreapp.model.Category;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Container
    private static final CustomMySqlContainer container = CustomMySqlContainer.getInstance();

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Sql(scripts = "classpath:database/category/add-test-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/remove-test-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Given existing categories, When findById called, Then returns correct category")
    void findById_ExistingCategory_ReturnsCategory() {
        Optional<Category> category = categoryRepository.findById(1L);

        assertEquals(true, category.isPresent());
        assertEquals("Programming", category.get().getName());
    }
}
