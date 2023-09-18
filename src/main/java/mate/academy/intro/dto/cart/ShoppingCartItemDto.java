package mate.academy.intro.dto.cart;

import lombok.Data;

@Data
public class ShoppingCartItemDto {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private Integer quantity;
}
