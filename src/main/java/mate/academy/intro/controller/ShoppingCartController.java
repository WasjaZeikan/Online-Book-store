package mate.academy.intro.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.cart.CreateShoppingCartItemsDto;
import mate.academy.intro.dto.cart.ShoppingCartDto;
import mate.academy.intro.dto.cart.ShoppingCartItemsDto;
import mate.academy.intro.dto.cart.UpdateShoppingCartItemsDto;
import mate.academy.intro.service.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartDto getCurrentUserShoppingCart(Authentication authentication) {
        return shoppingCartService.get(authentication);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartItemsDto addBookToShoppingCart(Authentication authentication,
            @Valid @RequestBody CreateShoppingCartItemsDto cartItemsDto) {
        return shoppingCartService.addBookToShoppingCart(authentication, cartItemsDto);
    }

    @PutMapping(value = "/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartItemsDto updateBookQuantity(Authentication authentication,
                                                   @Valid @RequestBody
                                                   UpdateShoppingCartItemsDto cartItemsDto,
                                                   @PathVariable Long cartItemId) {
        return shoppingCartService.updateBookQuantity(authentication, cartItemsDto, cartItemId);
    }

    @DeleteMapping(value = "/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    public void deleteBookFromShoppingCart(@PathVariable Long cartItemId) {
        shoppingCartService.deleteBookFromShoppingCart(cartItemId);
    }
}
