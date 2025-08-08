package com.fl.ecommerce.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fl.ecommerce.model.enums.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

@Entity
@Table(name = "pedidos")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación Muchos a Uno: Un pedido pertenece a un solo usuario.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private User usuario;

    @Column(nullable = false)
    private LocalDateTime fechaPedido;

    // Enum para el estado del pedido, mapeado como String en la base de datos.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus estadoPedido;

    @DecimalMin(value = "0.0", message = "El total del pedido no puede ser negativo.")
    @Column(nullable = false)
    private BigDecimal total;

    // Relación Uno a Muchos: Un pedido puede tener muchos detalles de pedido.
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> detallePedidos;
}
