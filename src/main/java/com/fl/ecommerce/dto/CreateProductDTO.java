package com.fl.ecommerce.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO para la creación de un nuevo producto.
 */
@Data
public class CreateProductDTO {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    private String descripcion;
    @NotNull(message = "El precio no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El precio debe ser positivo")
    private BigDecimal precio;

    //No iria aca ya que luego hariamos desde pedidos la gestion del stock
    /*@NotNull(message = "La cantidad en stock no puede ser nula")
    @Min(value = 0, message = "La cantidad en stock no puede ser negativa")
    private Integer cantidadEnStock; */

    /* No le pasamos el creador prq luego lo tomamos desde el contexto */
}
