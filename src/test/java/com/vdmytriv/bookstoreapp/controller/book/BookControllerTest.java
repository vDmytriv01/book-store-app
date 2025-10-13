package com.vdmytriv.bookstoreapp.controller.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdmytriv.bookstoreapp.config.CustomMySqlContainer;
import com.vdmytriv.bookstoreapp.dto.book.BookDto;
import com.vdmytriv.bookstoreapp.dto.book.CreateBookRequestDto;
import com.vdmytriv.bookstoreapp.dto.book.UpdateBookRequestDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class BookControllerTest {

    @Container
    private static final CustomMySqlContainer container = CustomMySqlContainer.getInstance();

    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext context) {
        container.start();
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Sql(scripts = "classpath:database/category/add-test-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/remove-test-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Given valid book data, when creating a book, then it is successfully created")
    void createBook_ValidRequestDto_Success() throws Exception {

        CreateBookRequestDto createBookRequest = new CreateBookRequestDto(
                "Effective Java",
                "Joshua Bloch",
                "978-3-16-148410-0",
                BigDecimal.valueOf(49.99),
                "A book about Java best practices.",
                "https://example.com/image.jpg",
                List.of(1L)
        );

        String jsonRequest = objectMapper.writeValueAsString(createBookRequest);

        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        assertNotNull(actual.id());
        assertEquals(createBookRequest.title(), actual.title());
        assertEquals(createBookRequest.author(), actual.author());
        assertEquals(createBookRequest.isbn(), actual.isbn());
        assertEquals(createBookRequest.price(), actual.price());
        assertEquals(createBookRequest.description(), actual.description());
        assertEquals(createBookRequest.coverImage(), actual.coverImage());
        assertEquals(createBookRequest.categoryIds(), actual.categoryIds());
    }

    @Sql(scripts = {
            "classpath:database/category/add-test-category.sql",
            "classpath:database/book/add-test-book.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book/remove-test-book.sql",
            "classpath:database/category/remove-test-category.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user")
    @Test
    @DisplayName("Given books exist, when getting all books, then return paginated list")
    void getAllBooks_ValidRequestDto_Success() throws Exception {
        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Map<String, Object> page = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });
        List<BookDto> books = objectMapper.convertValue(page.get("content"), new TypeReference<>() {
        });

        assertFalse(books.isEmpty());
        BookDto actual = books.get(0);

        assertNotNull(actual.id());
        assertEquals("Effective Java", actual.title());
        assertEquals("Joshua Bloch", actual.author());
        assertEquals("978-3-16-148410-1", actual.isbn());
        assertEquals(BigDecimal.valueOf(49.99), actual.price());
        assertEquals("A book about Java best practices.", actual.description());
        assertEquals("https://example.com/image.jpg", actual.coverImage());
        assertEquals(List.of(1L), actual.categoryIds());
    }

    @Sql(scripts = {
            "classpath:database/category/add-test-category.sql",
            "classpath:database/book/add-test-book.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book/remove-test-book.sql",
            "classpath:database/category/remove-test-category.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Given existing book, when updating, then it is updated successfully")
    void updateBook_ValidRequestDto_Success() throws Exception {
        UpdateBookRequestDto updateBookRequestDto = new UpdateBookRequestDto(
                "Effective Java Updated",
                "Joshua Bloch",
                "978-3-16-148410-0",
                BigDecimal.valueOf(59.99),
                "Updated description.",
                "https://example.com/image-new.jpg",
                List.of(1L)
        );

        String jsonRequest = objectMapper.writeValueAsString(updateBookRequestDto);

        MvcResult result = mockMvc.perform(put("/books/{id}", 1L)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        assertNotNull(actual.id());
        assertEquals(updateBookRequestDto.title(), actual.title());
        assertEquals(updateBookRequestDto.author(), actual.author());
        assertEquals(updateBookRequestDto.isbn(), actual.isbn());
        assertEquals(updateBookRequestDto.price(), actual.price());
        assertEquals(updateBookRequestDto.description(), actual.description());
        assertEquals(updateBookRequestDto.coverImage(), actual.coverImage());
        assertEquals(updateBookRequestDto.categoryIds(), actual.categoryIds());
    }
}
