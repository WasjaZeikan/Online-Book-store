package mate.academy.intro.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mate.academy.intro.model.Order;

@Data
public class UpdateOrderStatusDto {
    @NotNull
    private Order.Status status;
}
