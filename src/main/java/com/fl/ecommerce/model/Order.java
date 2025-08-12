package com.fl.ecommerce.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private BigDecimal total = BigDecimal.ZERO;

    // Relación Uno a Muchos: Un pedido puede tener muchos detalles de pedido.
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> detalles = new ArrayList<>();

    // Método público para agregar producto
    public void addProduct(Product producto, int cantidad) {
        validarCantidad(cantidad);

        OrderDetail detalle = buscarDetalle(producto);

        if (detalle != null) {
            actualizarCantidad(detalle, cantidad);
        } else {
            crearNuevoDetalle(producto, cantidad);
        }

        recalcularTotal();
    }

    public void removeProduct(Product producto) {
        OrderDetail detalle = buscarDetalle(producto);

        if (detalle == null) {
            throw new IllegalArgumentException("El producto no está en el pedido");
        }

        detalles.remove(detalle); // Gracias a orphanRemoval = true, JPA lo elimina
        recalcularTotal();
    }

    // Métodos privados auxiliares

    private void validarCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
    }

    private OrderDetail buscarDetalle(Product producto) {
        return detalles.stream()
            .filter(d -> d.getProducto().equals(producto))
            .findFirst()
            .orElse(null);
    }

    private void actualizarCantidad(OrderDetail detalle, int cantidad) {
        detalle.setCantidad(detalle.getCantidad() + cantidad);
    }

    private void crearNuevoDetalle(Product producto, int cantidad) {
        OrderDetail nuevoDetalle = new OrderDetail();
        nuevoDetalle.setPedido(this);
        nuevoDetalle.setProducto(producto);
        nuevoDetalle.setCantidad(cantidad);
        nuevoDetalle.setPrecioUnitario(producto.getPrecio());
        detalles.add(nuevoDetalle);
    }

    private void recalcularTotal() {
        total = detalles.stream()
            .map(d -> d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
