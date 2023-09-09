package mate.academy.intro.mapper.impl;

import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.intro.dto.cart.ShoppingCartDto;
import mate.academy.intro.dto.cart.ShoppingCartItemsDto;
import mate.academy.intro.mapper.ShoppingCartMapper;
import mate.academy.intro.model.CartItem;
import mate.academy.intro.model.ShoppingCart;
import org.springframework.stereotype.Component;

@Component
public class ShoppingCartMapperImpl implements ShoppingCartMapper {
    @Override
    public ShoppingCartDto toDto(ShoppingCart shoppingCart) {
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setId(shoppingCart.getId());
        shoppingCartDto.setUserId(shoppingCart.getUser().getId());
        Set<ShoppingCartItemsDto> cartItemsDtoSet = shoppingCart.getCartItems().stream()
                        .map(this::toCartItemsDto)
                                .collect(Collectors.toSet());
        shoppingCartDto.setCartItems(cartItemsDtoSet);
        return shoppingCartDto;
    }

    @Override
    public ShoppingCartItemsDto toCartItemsDto(CartItem cartItem) {
        ShoppingCartItemsDto shoppingCartItemsDto = new ShoppingCartItemsDto();
        shoppingCartItemsDto.setId(cartItem.getId());
        shoppingCartItemsDto.setBookId(cartItem.getBook().getId());
        shoppingCartItemsDto.setBookTitle(cartItem.getBook().getTitle());
        shoppingCartItemsDto.setQuantity(cartItem.getQuantity());
        return shoppingCartItemsDto;
    }
}
