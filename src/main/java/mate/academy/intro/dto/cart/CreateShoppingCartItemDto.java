package mate.academy.intro.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateShoppingCartItemDto {
    @NotNull
    private Long bookId;
    @NotNull
    @Min(1)
    private Integer quantity;
}
