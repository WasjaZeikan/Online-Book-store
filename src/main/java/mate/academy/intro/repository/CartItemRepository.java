package mate.academy.intro.repository;

import java.util.Optional;
import mate.academy.intro.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    @Override
    @Query("SELECT ci FROM CartItem ci LEFT JOIN FETCH ci.book WHERE ci.id=:id")
    Optional<CartItem> findById(Long id);
}
