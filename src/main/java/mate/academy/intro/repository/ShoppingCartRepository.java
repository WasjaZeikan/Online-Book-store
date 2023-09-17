package mate.academy.intro.repository;

import java.util.Optional;
import mate.academy.intro.model.ShoppingCart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
    @EntityGraph(attributePaths = {"user", "cartItems"})
    Optional<ShoppingCart> findById(Long id);
}
