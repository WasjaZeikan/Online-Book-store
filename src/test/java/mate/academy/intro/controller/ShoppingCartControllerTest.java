package mate.academy.intro.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;
import javax.sql.DataSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import mate.academy.intro.dto.cart.CreateShoppingCartItemDto;
import mate.academy.intro.dto.cart.ShoppingCartDto;
import mate.academy.intro.dto.cart.ShoppingCartItemDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingCartControllerTest {
    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserDetailsService userDetailsService;

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
                    new ClassPathResource("database/shopping-carts/add-shopping-cart.sql"));
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
                    new ClassPathResource("database/shopping-carts/remove-shopping-cart.sql"));
        }
    }

    @Test
    @DisplayName("Verify getCurrentUserShoppingCart() method works correctly")
    void getCurrentUserShoppingCart_validUserId_ReturnShoppingCartDto() throws Exception {
        ShoppingCartDto expected = new ShoppingCartDto();
        expected.setId(1L);
        expected.setUserId(1L);
        expected.setCartItems(Set.of());
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin@admin.com");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());

        TestSecurityContextHolder.setAuthentication(authentication);
        MvcResult result = mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                ShoppingCartDto.class);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify addBookToShoppingCart() method works correctly")
    void addBookToShoppingCart_validUserIdAndCreateShoppingCartItemDto_ReturnValidShoppingCartItemDto() throws Exception {
        Long id = 1L;
        ShoppingCartItemDto expected = new ShoppingCartItemDto();
        expected.setBookTitle("Title");
        expected.setBookId(id);
        expected.setId(id);
        expected.setQuantity(5);
        CreateShoppingCartItemDto createShoppingCartItemDto = new CreateShoppingCartItemDto();
        createShoppingCartItemDto.setQuantity(5);
        createShoppingCartItemDto.setBookId(id);
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin@admin.com");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());

        String jsonRequest = objectMapper.writeValueAsString(createShoppingCartItemDto);
        TestSecurityContextHolder.setAuthentication(authentication);
        MvcResult result = mockMvc.perform(post("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartItemDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                ShoppingCartItemDto.class);
        assertEquals(expected, actual);
    }
}