package mate.academy.intro.repository;

import java.util.List;
import java.util.Optional;
import mate.academy.intro.model.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    @Query("FROM OrderItem oi LEFT JOIN FETCH oi.order o LEFT JOIN FETCH oi.book "
            + "WHERE o.id=:orderId")
    List<OrderItem> findAllByOrderId(Long orderId, Pageable pageable);

    @Query("FROM OrderItem oi LEFT JOIN FETCH oi.order o LEFT JOIN FETCH oi.book "
            + "WHERE o.id=:orderId AND oi.id=:id")
    Optional<OrderItem> findByIdAndOrderId(Long id, Long orderId);
}
