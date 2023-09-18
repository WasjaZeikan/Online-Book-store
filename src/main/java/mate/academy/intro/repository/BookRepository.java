package mate.academy.intro.repository;

import java.util.Optional;
import mate.academy.intro.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query(value = "SELECT b FROM Book b JOIN b.categories c WHERE c.id=:categoryId")
    Page<Book> findAllByCategoryId(Pageable pageable, Long categoryId);

    @EntityGraph(attributePaths = {"categories"})
    Optional<Book> findById(Long id);

    @EntityGraph(attributePaths = {"categories"})
    Page<Book> findAll(Pageable pageable);
}
