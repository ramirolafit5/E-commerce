package com.fl.ecommerce.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * DTO para la respuesta de un producto, mostrando la información relevante.
 */
@Data
public class ProductResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer cantidadEnStock;
    private String creadorUsername;
}
