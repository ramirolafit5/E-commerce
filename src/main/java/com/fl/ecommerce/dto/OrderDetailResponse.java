package com.fl.ecommerce.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderDetailResponse {
    private String producto;     // nombre del producto
    private int cantidad;
    private BigDecimal precioUnitario;
}