package com.fl.ecommerce.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para actualizar un producto. Los campos pueden ser nulos si no se desean actualizar.
 */
@Data
public class UpdateProductDTO {
    @Size(min = 3, max = 255, message = "El nombre debe tener entre 3 y 255 caracteres")
    private String nombre;
    private String descripcion;
    @DecimalMin(value = "0.01", message = "El precio debe ser positivo")
    private BigDecimal precio;
}
