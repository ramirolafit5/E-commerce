package com.fl.ecommerce.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fl.ecommerce.model.enums.OrderStatus;

import lombok.Data;

@Data
public class OrderAndDetailsResponseDTO {
    private Long id;
    private String usuarioNombre;
    private LocalDateTime fechaPedido;
    private OrderStatus estadoPedido;
    private BigDecimal total;
    private List<OrderDetailResponse> detalles;
}
