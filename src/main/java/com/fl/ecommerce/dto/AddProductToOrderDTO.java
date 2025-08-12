package com.fl.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddProductToOrderDTO {
    @NotNull
    private Long pedidoId;

    @NotNull
    private Long productoId;

    @Min(1)
    private int cantidad;
}
