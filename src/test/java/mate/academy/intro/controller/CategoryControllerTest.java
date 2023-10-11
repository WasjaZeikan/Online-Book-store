package mate.academy.intro.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import mate.academy.intro.dto.category.CategoryDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource,
                          @Autowired WebApplicationContext applicationContext) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/books/add-two-categories.sql"));
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    private static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/books/remove-two-categories.sql"));
        }
    }

    @Test
    @DisplayName("Verify createCategory() method works correctly")
    @WithMockUser(username = "admin", authorities = {"USER", "ADMIN"})
    void createCategory_validCategoryDto_ReturnCorrectCategoryDto() throws Exception {
        CategoryDto expected = new CategoryDto();
        expected.setName("Category 3");
        String jsonRequest = objectMapper.writeValueAsString(expected);
        MvcResult result = mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("Verify getAll() method works correctly")
    @WithMockUser(username = "admin", authorities = {"USER", "ADMIN"})
    void getAll_twoCategories_ReturnTwoCategories() throws Exception {
        CategoryDto categoryDto1 = new CategoryDto();
        categoryDto1.setId(1L);
        categoryDto1.setName("Category 1");
        CategoryDto categoryDto2 = new CategoryDto();
        categoryDto2.setId(2L);
        categoryDto2.setName("Category 2");
        List<CategoryDto> expected = List.of(categoryDto1, categoryDto2);

        MvcResult result = mockMvc.perform(get("/api/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<CategoryDto> actual = List.of(objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto[].class));
        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify getCategoryById() method works correctly")
    @WithMockUser(username = "admin", authorities = {"USER", "ADMIN"})
    void getCategoryById_validCategoryId_ReturnCorrectCategoryDto() throws Exception {
        CategoryDto expected = new CategoryDto();
        expected.setId(1L);
        expected.setName("Category 1");

        MvcResult result = mockMvc.perform(get("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);
        assertEquals(expected, actual);
    }
}