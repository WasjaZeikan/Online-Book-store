package mate.academy.intro.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.cart.CreateShoppingCartItemDto;
import mate.academy.intro.dto.cart.ShoppingCartDto;
import mate.academy.intro.dto.cart.ShoppingCartItemDto;
import mate.academy.intro.dto.cart.UpdateShoppingCartItemDto;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.mapper.ShoppingCartMapper;
import mate.academy.intro.model.CartItem;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.repository.BookRepository;
import mate.academy.intro.repository.CartItemRepository;
import mate.academy.intro.repository.ShoppingCartRepository;
import mate.academy.intro.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final BookRepository bookRepository;

    @Override
    public ShoppingCartDto getByUserId(Long userId) {
        return shoppingCartMapper.toDto(getCurrentUserShoppingCart(userId));
    }

    @Override
    public ShoppingCartItemDto addBookToShoppingCart(Long userId,
                                                     CreateShoppingCartItemDto cartItemsDto) {
        CartItem cartItem = new CartItem();
        ShoppingCart shoppingCart = getCurrentUserShoppingCart(userId);
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setQuantity(cartItemsDto.getQuantity());
        cartItem.setBook(bookRepository.findById(cartItemsDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find "
                        + "book with id: " + cartItemsDto.getBookId())));
        cartItem = cartItemRepository.save(cartItem);
        shoppingCart.getCartItems().add(cartItem);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toCartItemDto(cartItem);
    }

    @Override
    public ShoppingCartItemDto updateBookQuantity(UpdateShoppingCartItemDto cartItemsDto,
                                                  Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Can't find cartItem with id: " + cartItemId));
        cartItem.setQuantity(cartItemsDto.getQuantity());
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toCartItemDto(cartItemRepository.findById(cartItemId).get());
    }

    @Override
    public void deleteBookFromShoppingCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    private ShoppingCart getCurrentUserShoppingCart(Long userId) {
        return shoppingCartRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find "
                        + "shopping cart for current user"));
    }
}
