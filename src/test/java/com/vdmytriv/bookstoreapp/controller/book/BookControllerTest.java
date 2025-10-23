package com.vdmytriv.bookstoreapp.controller.book;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import com.vdmytriv.bookstoreapp.util.TestDataFactory;
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

        CreateBookRequestDto createBookRequest = TestDataFactory.createBookRequestDto();

        String jsonRequest = objectMapper.writeValueAsString(createBookRequest);

        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        BookDto expected = new BookDto(
                actual.id(),
                createBookRequest.title(),
                createBookRequest.author(),
                createBookRequest.isbn(),
                createBookRequest.price(),
                createBookRequest.description(),
                createBookRequest.coverImage(),
                createBookRequest.categoryIds()
        );

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
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
        BookDto expected = TestDataFactory.createBookDto()
                .toBuilder()
                .id(actual.id())
                .build();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
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
        UpdateBookRequestDto updateBookRequestDto = TestDataFactory.createUpdateBookRequest();

        String jsonRequest = objectMapper.writeValueAsString(updateBookRequestDto);

        MvcResult result = mockMvc.perform(put("/books/{id}", 1L)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        BookDto expected = TestDataFactory.createBookDto()
                .toBuilder()
                .id(actual.id())
                .title(updateBookRequestDto.title())
                .isbn(updateBookRequestDto.isbn())
                .price(updateBookRequestDto.price())
                .description(updateBookRequestDto.description())
                .coverImage(updateBookRequestDto.coverImage())
                .categoryIds(updateBookRequestDto.categoryIds())
                .build();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Given invalid book data, when creating a book, then return 400 Bad Request")
    void createBook_InvalidRequest_ReturnsBadRequest() throws Exception {
        CreateBookRequestDto invalidRequest = new CreateBookRequestDto(
                "", "", "", null, "", "", List.of()
        );

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Given invalid update data, when updating a book, then return 400 Bad Request")
    void updateBook_InvalidRequest_ReturnsBadRequest() throws Exception {
        UpdateBookRequestDto invalidUpdate = new UpdateBookRequestDto(
                "", "", "", null, "", "", List.of()
        );

        mockMvc.perform(put("/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdate)))
                .andExpect(status().isBadRequest());
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
    @DisplayName("Given existing book, when deleting by id, then it is removed successfully")
    void deleteBookById_Success() throws Exception {

        mockMvc.perform(delete("/books/{id}", 1L))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/books/{id}", 1L)
                        .with(user("user").roles("USER")))
                .andExpect(status().isNotFound());
    }

}
