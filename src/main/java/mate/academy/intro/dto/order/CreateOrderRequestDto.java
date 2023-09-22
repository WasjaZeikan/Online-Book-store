package mate.academy.intro.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderRequestDto {
    @NotNull
    private String shippingAddress;
}
