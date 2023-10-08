package mate.academy.intro.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import mate.academy.intro.model.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @Sql(scripts = { "classpath:database/books/add-two-categories.sql",
            "classpath:database/books/add-three-books.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/books/remove-three-books.sql",
            "classpath:database/books/remove-two-categories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Verify findAllByCategoryId() method works correctly")
    void findAllByCategoryId_ThreeBooks_ReturnTwoBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        Long categoryId = 1L;
        Book firstBook = new Book();
        firstBook.setId(1L);
        firstBook.setTitle("Book 1");
        Book secondBook = new Book();
        secondBook.setId(3L);
        secondBook.setTitle("Book 3");
        Page<Book> expected = new PageImpl<>(List.of(firstBook, secondBook), pageable, 2L);

        Page<Book> actual = bookRepository.findAllByCategoryId(pageable, categoryId);

        assertEquals(expected.getTotalElements(), actual.getTotalElements());
        assertEquals(expected.stream().toList().get(0).getTitle(),
                actual.stream().toList().get(0).getTitle());
        assertEquals(expected.stream().toList().get(1).getTitle(),
                actual.stream().toList().get(1).getTitle());
    }
}