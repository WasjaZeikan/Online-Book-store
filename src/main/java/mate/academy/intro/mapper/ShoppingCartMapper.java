package mate.academy.intro.mapper;

import mate.academy.intro.dto.cart.ShoppingCartDto;
import mate.academy.intro.dto.cart.ShoppingCartItemsDto;
import mate.academy.intro.model.CartItem;
import mate.academy.intro.model.ShoppingCart;

public interface ShoppingCartMapper {
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    ShoppingCartItemsDto toCartItemsDto(CartItem cartItem);
}
