package mate.academy.intro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.order.CreateOrderRequestDto;
import mate.academy.intro.dto.order.OrderDto;
import mate.academy.intro.dto.order.OrderItemDto;
import mate.academy.intro.dto.order.UpdateOrderStatusDto;
import mate.academy.intro.model.User;
import mate.academy.intro.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/orders")
@Tag(name = "Orders managing", description = "Endpoints for managing orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get orders history",
            description = "Get a list of orders that current user made")
    List<OrderDto> getOrderHistory(Authentication authentication, Pageable pageable) {
        User currentUser = (User) authentication.getPrincipal();
        return orderService.getOrderHistory(currentUser.getId(), pageable);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create order",
            description = "Create a new order from current user's shopping cart")
    OrderDto createOrder(Authentication authentication, @Valid
                        @RequestBody CreateOrderRequestDto orderRequestDto) {
        User currentUser = (User) authentication.getPrincipal();
        return orderService.createOrder(currentUser.getId(), orderRequestDto);
    }

    @PatchMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Update order status",
            description = "Update the status of an order with specified id")
    OrderDto updateOrderStatus(@PathVariable("id") Long orderId, @Valid
                                @RequestBody UpdateOrderStatusDto orderStatusDto) {
        return orderService.updateOrderStatus(orderId, orderStatusDto);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get order items",
            description = "Get a list of items of an order with a specified id")
    List<OrderItemDto> getOrderItems(@PathVariable Long orderId, Pageable pageable) {
        return orderService.getOrderItems(orderId, pageable);
    }

    @GetMapping("{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get order item by id",
            description = "Get the specified order item with a specified id")
    OrderItemDto getOrderItemById(@PathVariable Long orderId, @PathVariable Long itemId) {
        return orderService
                .getOrderItemById(orderId, itemId);
    }
}
