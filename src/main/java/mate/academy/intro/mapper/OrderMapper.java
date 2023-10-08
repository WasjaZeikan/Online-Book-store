package mate.academy.intro.mapper;

import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.order.OrderDto;
import mate.academy.intro.dto.order.OrderItemDto;
import mate.academy.intro.model.Order;
import mate.academy.intro.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "order.user.id")
    OrderDto toOrderDto(Order order);

    @Mapping(target = "bookId", source = "orderItem.book.id")
    OrderItemDto toOrderItemDto(OrderItem orderItem);
}
