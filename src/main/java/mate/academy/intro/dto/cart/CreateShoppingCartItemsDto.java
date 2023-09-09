package mate.academy.intro.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateShoppingCartItemsDto {
    @NotNull
    private Long bookId;
    @NotNull
    @Min(1L)
    private Integer quantity;
}
