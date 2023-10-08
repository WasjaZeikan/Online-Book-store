package mate.academy.intro.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import mate.academy.intro.dto.book.BookDto;
import mate.academy.intro.dto.book.CreateBookRequestDto;
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
class BookControllerTest {
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
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/books/add-three-books.sql"));
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
                    new ClassPathResource("database/books/remove-three-books.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/books/remove-two-categories.sql"));
        }
    }

    @Test
    @DisplayName("Verify getAll() method works correctly")
    @WithMockUser(username = "admin", authorities = {"USER", "ADMIN"})
    void getAll_ThreeBooks_ReturnThreeBooks() throws Exception {
        BookDto firstBook = new BookDto();
        firstBook.setId(1L);
        firstBook.setTitle("Book 1");
        firstBook.setAuthor("Author 1");
        firstBook.setIsbn("1234578");
        firstBook.setPrice(BigDecimal.valueOf(50.55));
        firstBook.setCategoryIds(List.of(1L));
        BookDto secondBook = new BookDto();
        secondBook.setId(2L);
        secondBook.setTitle("Book 2");
        secondBook.setAuthor("Author 2");
        secondBook.setIsbn("12345789");
        secondBook.setPrice(BigDecimal.valueOf(150.99));
        secondBook.setCategoryIds(List.of(2L));
        BookDto thirdBook = new BookDto();
        thirdBook.setId(3L);
        thirdBook.setTitle("Book 3");
        thirdBook.setAuthor("Author 1");
        thirdBook.setIsbn("123457890");
        thirdBook.setPrice(BigDecimal.valueOf(14.99));
        thirdBook.setCategoryIds(List.of(1L));
        List<BookDto> expected = List.of(firstBook, secondBook, thirdBook);

        MvcResult result = mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andReturn();


        List<BookDto> actual = List.of(objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto[].class));
        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify getBookById() method works correctly")
    @WithMockUser(username = "admin", authorities = {"USER", "ADMIN"})
    void getBookById_validBookId_ReturnValidBookDto() throws Exception {
        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setTitle("Book 1");
        expected.setAuthor("Author 1");
        expected.setIsbn("1234578");
        expected.setPrice(BigDecimal.valueOf(50.55));
        expected.setCategoryIds(List.of(1L));

        MvcResult result = mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify createBook() method works correctly")
    @WithMockUser(username = "admin", authorities = {"USER", "ADMIN"})
    void createBook_validCreateRequestDto_ReturnValidBookDto() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Harry Potter");
        requestDto.setAuthor("Joanne Rowling");
        requestDto.setCategoryIds(List.of(1L));
        requestDto.setPrice(BigDecimal.valueOf(100));
        requestDto.setIsbn("123456");

        BookDto expected = new BookDto();
        expected.setTitle("Harry Potter");
        expected.setAuthor("Joanne Rowling");
        expected.setCategoryIds(List.of(1L));
        expected.setPrice(BigDecimal.valueOf(100));
        expected.setIsbn("123456");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        EqualsBuilder.reflectionEquals(expected,actual,"id");
    }
}