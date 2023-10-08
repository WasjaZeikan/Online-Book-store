package mate.academy.intro.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.order.CreateOrderRequestDto;
import mate.academy.intro.dto.order.OrderDto;
import mate.academy.intro.dto.order.OrderItemDto;
import mate.academy.intro.dto.order.UpdateOrderStatusDto;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.mapper.OrderMapper;
import mate.academy.intro.model.CartItem;
import mate.academy.intro.model.Order;
import mate.academy.intro.model.OrderItem;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.repository.OrderItemRepository;
import mate.academy.intro.repository.OrderRepository;
import mate.academy.intro.repository.ShoppingCartRepository;
import mate.academy.intro.repository.UserRepository;
import mate.academy.intro.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public List<OrderDto> getOrderHistory(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable).stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }

    @Override
    public OrderDto createOrder(Long userId, CreateOrderRequestDto orderRequestDto) {
        Order order = new Order();
        order.setUser(userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find user with id: " + userId)));
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.Status.PENDING);
        order.setShippingAddress(orderRequestDto.getShippingAddress());
        ShoppingCart shoppingCart = shoppingCartRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find current user's shopping cart"));
        order.setTotal(getTotalFromCartItems(shoppingCart.getCartItems()));
        order = orderRepository.save(order);
        for (CartItem cartItem : shoppingCart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);
            orderItem.setPrice(BigDecimal.valueOf(cartItem.getQuantity())
                    .multiply(cartItem.getBook().getPrice()));
            orderItem.setBook(cartItem.getBook());
            orderItem = orderItemRepository.save(orderItem);
            order.getOrderItems().add(orderItem);
        }
        orderRepository.save(order);
        return orderMapper.toOrderDto(orderRepository.findById(order.getId()).get());
    }

    @Override
    public OrderDto updateOrderStatus(Long orderId, UpdateOrderStatusDto orderStatusDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Can't find order with id: " + orderId));
        order.setStatus(orderStatusDto.getStatus());
        orderRepository.save(order);
        return orderMapper.toOrderDto(orderRepository.findById(orderId).get());
    }

    @Override
    public List<OrderItemDto> getOrderItems(Long orderId, Pageable pageable) {
        return orderItemRepository.findAllByOrderId(orderId, pageable).stream()
                .map(orderMapper::toOrderItemDto)
                .toList();
    }

    @Override
    public OrderItemDto getOrderItemById(Long orderId, Long orderItemId) {
        return orderMapper.toOrderItemDto(orderItemRepository
                .findByIdAndOrderId(orderItemId, orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order item with id: " + orderItemId)));
    }

    private BigDecimal getTotalFromCartItems(Set<CartItem> cartItems) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            total = total.add(BigDecimal.valueOf(cartItem.getQuantity())
                    .multiply(cartItem.getBook().getPrice()));
        }
        return total;
    }
}
