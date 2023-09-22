package mate.academy.intro.repository;

import java.util.Optional;
import mate.academy.intro.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.user u LEFT JOIN FETCH o.orderItems oi "
            + "LEFT JOIN FETCH oi.book WHERE u.id=:userId")
    Page<Order> findAllByUserId(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "orderItems"})
    Optional<Order> findById(Long id);
}
