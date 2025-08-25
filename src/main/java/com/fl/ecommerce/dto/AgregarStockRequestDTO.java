package com.fl.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AgregarStockRequestDTO {
    @NotNull
    Long productoId;

    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    int cantidad;
}
