package mate.academy.intro.service;

import java.util.List;
import mate.academy.intro.dto.order.CreateOrderRequestDto;
import mate.academy.intro.dto.order.OrderDto;
import mate.academy.intro.dto.order.OrderItemDto;
import mate.academy.intro.dto.order.UpdateOrderStatusDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    List<OrderDto> getOrderHistory(Long userId, Pageable pageable);

    OrderDto createOrder(Long userId, CreateOrderRequestDto orderRequestDto);

    OrderDto updateOrderStatus(Long orderId, UpdateOrderStatusDto orderStatusDto);

    List<OrderItemDto> getOrderItems(Long orderId, Pageable pageable);

    OrderItemDto getOrderItemById(Long orderId, Long orderItemId);
}
