package mate.academy.intro.service;

import mate.academy.intro.dto.cart.CreateShoppingCartItemsDto;
import mate.academy.intro.dto.cart.ShoppingCartDto;
import mate.academy.intro.dto.cart.ShoppingCartItemsDto;
import mate.academy.intro.dto.cart.UpdateShoppingCartItemsDto;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {
    ShoppingCartDto get(Authentication authentication);

    ShoppingCartItemsDto addBookToShoppingCart(Authentication authentication,
                                               CreateShoppingCartItemsDto cartItemsDto);

    ShoppingCartItemsDto updateBookQuantity(Authentication authentication,
                                            UpdateShoppingCartItemsDto cartItemsDto,
                                            Long cartItemId);

    void deleteBookFromShoppingCart(Long cartItemId);
}
