package mate.academy.intro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.cart.CreateShoppingCartItemDto;
import mate.academy.intro.dto.cart.ShoppingCartDto;
import mate.academy.intro.dto.cart.ShoppingCartItemDto;
import mate.academy.intro.dto.cart.UpdateShoppingCartItemDto;
import mate.academy.intro.model.User;
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
@Tag(name = "Shopping cart management",
        description = "Endpoints for managing current user's shopping cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get shopping cart", description = "Get a current user's shopping cart")
    public ShoppingCartDto getCurrentUserShoppingCart(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return shoppingCartService.getByUserId(currentUser.getId());
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Add book to shopping cart",
            description = "Add a book with specified id to current user's shopping cart")
    public ShoppingCartItemDto addBookToShoppingCart(Authentication authentication,
                                                     @Valid @RequestBody
                                                     CreateShoppingCartItemDto cartItemsDto) {
        User currentUser = (User) authentication.getPrincipal();
        return shoppingCartService.addBookToShoppingCart(currentUser.getId(), cartItemsDto);
    }

    @PutMapping(value = "/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update book quantity",
            description = "Update the quantity of a book with a specified id")
    public ShoppingCartItemDto updateBookQuantity(@Valid @RequestBody
                                                   UpdateShoppingCartItemDto cartItemsDto,
                                                  @PathVariable Long cartItemId) {
        return shoppingCartService.updateBookQuantity(cartItemsDto, cartItemId);
    }

    @DeleteMapping(value = "/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Delete book from shopping cart",
            description = "Delete a book with a specified id "
                    + "from the current user's shopping cart")
    public void deleteBookFromShoppingCart(@PathVariable Long cartItemId) {
        shoppingCartService.deleteBookFromShoppingCart(cartItemId);
    }
}
