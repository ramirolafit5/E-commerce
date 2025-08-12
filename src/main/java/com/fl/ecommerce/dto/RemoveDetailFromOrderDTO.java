package com.fl.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RemoveDetailFromOrderDTO {
    @NotNull
    private Long pedidoId;

    @NotNull
    private Long productoId;
}
