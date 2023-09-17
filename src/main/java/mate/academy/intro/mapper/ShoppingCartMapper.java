package mate.academy.intro.mapper;

import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.cart.ShoppingCartDto;
import mate.academy.intro.dto.cart.ShoppingCartItemDto;
import mate.academy.intro.model.CartItem;
import mate.academy.intro.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {

    @Mapping(target = "userId", source = "shoppingCart.user.id")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @Mapping(target = "bookId", source = "cartItem.book.id")
    @Mapping(target = "bookTitle", source = "cartItem.book.title")
    ShoppingCartItemDto toCartItemDto(CartItem cartItem);
}
