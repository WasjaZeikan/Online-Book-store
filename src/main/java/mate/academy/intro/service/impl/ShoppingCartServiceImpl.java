package mate.academy.intro.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.cart.CreateShoppingCartItemsDto;
import mate.academy.intro.dto.cart.ShoppingCartDto;
import mate.academy.intro.dto.cart.ShoppingCartItemsDto;
import mate.academy.intro.dto.cart.UpdateShoppingCartItemsDto;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.mapper.ShoppingCartMapper;
import mate.academy.intro.model.CartItem;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.repository.BookRepository;
import mate.academy.intro.repository.CartItemRepository;
import mate.academy.intro.repository.ShoppingCartRepository;
import mate.academy.intro.service.ShoppingCartService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final BookRepository bookRepository;

    @Override
    public ShoppingCartDto get(Authentication authentication) {
        return shoppingCartMapper.toDto(getCurrentUserShoppingCart(authentication));
    }

    @Override
    public ShoppingCartItemsDto addBookToShoppingCart(Authentication authentication,
                                                      CreateShoppingCartItemsDto cartItemsDto) {
        CartItem cartItem = new CartItem();
        ShoppingCart shoppingCart = getCurrentUserShoppingCart(authentication);
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setQuantity(cartItemsDto.getQuantity());
        cartItem.setBook(bookRepository.findById(cartItemsDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find "
                        + "book with id: " + cartItemsDto.getBookId())));
        cartItem = cartItemRepository.save(cartItem);
        shoppingCart.getCartItems().add(cartItem);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toCartItemsDto(cartItem);
    }

    @Override
    public ShoppingCartItemsDto updateBookQuantity(Authentication authentication,
                                                   UpdateShoppingCartItemsDto cartItemsDto,
                                                   Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Can't find cartItem with id: " + cartItemId));
        cartItem.setQuantity(cartItemsDto.getQuantity());
        cartItem = cartItemRepository.save(cartItem);
        return shoppingCartMapper.toCartItemsDto(cartItem);
    }

    @Override
    public void deleteBookFromShoppingCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    private ShoppingCart getCurrentUserShoppingCart(Authentication authentication) {
        return shoppingCartRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("Can't find "
                        + "shopping cart for current user"));
    }
}
