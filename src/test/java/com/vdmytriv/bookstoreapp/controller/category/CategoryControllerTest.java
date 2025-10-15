package com.vdmytriv.bookstoreapp.controller.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdmytriv.bookstoreapp.config.CustomMySqlContainer;
import com.vdmytriv.bookstoreapp.dto.category.CategoryDto;
import com.vdmytriv.bookstoreapp.dto.category.UpdateCategoryRequestDto;
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
class CategoryControllerTest {

    @Container
    private static final CustomMySqlContainer container = CustomMySqlContainer.getInstance();

    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void setUp(@Autowired WebApplicationContext context) {
        container.start();
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Sql(scripts = "classpath:database/category/add-test-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/remove-test-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user")
    @Test
    @DisplayName("Given categories exist, when getAll called, then return paginated list")
    void getAllCategories_Success() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Map<String, Object> page =
                objectMapper.readValue(jsonResponse, new TypeReference<>() {
                });
        List<CategoryDto> categories =
                objectMapper.convertValue(page.get("content"), new TypeReference<>() {
                });

        assertFalse(categories.isEmpty());
        CategoryDto actual = categories.get(0);

        CategoryDto expected = new CategoryDto(
                actual.id(),
                "Programming",
                actual.description()
        );
        assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Given valid category data, when create called, then category is created")
    void createCategory_Success() throws Exception {
        CategoryDto request = new CategoryDto(null, "New Category", "Description");
        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);

        CategoryDto expected = new CategoryDto(
                actual.id(),
                request.name(),
                request.description()
        );
        assertEquals(expected, actual);
    }

    @Sql(scripts = "classpath:database/category/add-test-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/remove-test-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Given existing category, when updating, then updated successfully")
    void updateCategory_Success() throws Exception {
        UpdateCategoryRequestDto request =
                new UpdateCategoryRequestDto("Updated Category", "Updated description");
        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(put("/categories/{id}", 1L)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);

        CategoryDto expected = new CategoryDto(
                actual.id(),
                request.name(),
                request.description()
        );
        assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Given invalid category data, when creating, then return 400 Bad Request")
    void createCategory_InvalidRequest_ReturnsBadRequest() throws Exception {
        CategoryDto invalid = new CategoryDto(null, "", "");
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Given invalid update data, when updating, then return 400 Bad Request")
    void updateCategory_InvalidRequest_ReturnsBadRequest() throws Exception {
        UpdateCategoryRequestDto invalid = new UpdateCategoryRequestDto("", "");
        mockMvc.perform(put("/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Sql(scripts = "classpath:database/category/add-test-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/remove-test-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Given existing category, when deleting by id, then it is removed successfully")
    void deleteCategoryById_Success() throws Exception {
        mockMvc.perform(delete("/categories/{id}", 1L))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/categories/{id}", 1L))
                .andExpect(status().isNotFound());
    }
}
