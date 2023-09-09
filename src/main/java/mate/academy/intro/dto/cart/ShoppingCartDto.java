package mate.academy.intro.dto.cart;

import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<ShoppingCartItemsDto> cartItems;
}
