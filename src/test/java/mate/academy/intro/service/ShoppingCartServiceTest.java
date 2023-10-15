package mate.academy.intro.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mate.academy.intro.dto.cart.CreateShoppingCartItemDto;
import mate.academy.intro.dto.cart.ShoppingCartDto;
import mate.academy.intro.dto.cart.ShoppingCartItemDto;
import mate.academy.intro.mapper.ShoppingCartMapper;
import mate.academy.intro.model.Book;
import mate.academy.intro.model.CartItem;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.model.User;
import mate.academy.intro.repository.BookRepository;
import mate.academy.intro.repository.CartItemRepository;
import mate.academy.intro.repository.ShoppingCartRepository;
import mate.academy.intro.service.impl.ShoppingCartServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private BookRepository bookRepository;

    @Test
    @DisplayName("Verify getByUserId() method works correctly")
    void getByUserId_validUserId_ReturnValidShoppingCartDto() {
        ShoppingCartDto expected = new ShoppingCartDto();
        expected.setUserId(1L);
        expected.setId(1L);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        User user = new User();
        user.setId(1L);
        shoppingCart.setUser(user);

        when(shoppingCartRepository.findById(anyLong())).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartMapper.toDto(any(ShoppingCart.class))).thenReturn(expected);
        ShoppingCartDto actual = shoppingCartService.getByUserId(1L);

        assertEquals(expected, actual);
        verify(shoppingCartMapper, times(1)).toDto(any(ShoppingCart.class));
        verify(shoppingCartRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(shoppingCartMapper, shoppingCartRepository);
    }

    @Test
    @DisplayName("Verify addBookToShoppingCart() method works correctly")
    void addBookToShoppingCart_validUserIdAndShoppingCartDto_ReturnValidShoppingCartItemDto() {
        CreateShoppingCartItemDto cartItemDto = new CreateShoppingCartItemDto();
        Long id = 1L;
        cartItemDto.setBookId(id);
        cartItemDto.setQuantity(5);
        ShoppingCartItemDto expected = new ShoppingCartItemDto();
        expected.setBookId(id);
        expected.setId(id);
        expected.setQuantity(5);
        expected.setBookTitle("Title");
        ShoppingCart shoppingCart = new ShoppingCart();
        User user = new User();
        user.setId(id);
        shoppingCart.setUser(user);
        shoppingCart.setId(id);
        Book book = new Book();
        book.setId(id);
        book.setTitle("Title");
        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setQuantity(5);
        cartItem.setId(id);

        when(shoppingCartRepository.findById(anyLong())).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartMapper.toCartItemDto(any(CartItem.class))).thenReturn(expected);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(shoppingCart);
        ShoppingCartItemDto actual = shoppingCartService.addBookToShoppingCart(id, cartItemDto);

        assertEquals(expected, actual);
        verifyNoMoreInteractions(shoppingCartMapper, shoppingCartRepository, bookRepository, cartItemRepository);
    }
}