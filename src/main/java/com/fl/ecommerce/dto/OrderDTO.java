package com.fl.ecommerce.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fl.ecommerce.model.enums.OrderStatus;

import lombok.Data;

@Data
public class OrderDTO {
    private Long id;
    private LocalDateTime fechaPedido;
    private OrderStatus estadoPedido;
    private BigDecimal total;
    private String usuarioNombre;
}
