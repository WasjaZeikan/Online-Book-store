package mate.academy.intro.service;

import mate.academy.intro.dto.cart.CreateShoppingCartItemDto;
import mate.academy.intro.dto.cart.ShoppingCartDto;
import mate.academy.intro.dto.cart.ShoppingCartItemDto;
import mate.academy.intro.dto.cart.UpdateShoppingCartItemDto;

public interface ShoppingCartService {
    ShoppingCartDto getByUserId(Long userId);

    ShoppingCartItemDto addBookToShoppingCart(Long userId,
                                              CreateShoppingCartItemDto cartItemsDto);

    ShoppingCartItemDto updateBookQuantity(UpdateShoppingCartItemDto cartItemsDto,
                                           Long cartItemId);

    void deleteBookFromShoppingCart(Long cartItemId);
}
