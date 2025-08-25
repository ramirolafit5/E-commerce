package com.fl.ecommerce.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "productos")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del producto no puede estar vacío.")
    @Size(max = 255)
    @Column(nullable = false)
    private String nombre;

    @Size(max = 1000)
    private String descripcion;

    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que cero.")
    @Column(nullable = false)
    private BigDecimal precio;

    @Min(value = 0, message = "La cantidad en stock no puede ser negativa.")
    @Column(nullable = false)
    private Integer cantidadEnStock = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private User creador;

    public void incrementarStock() {
        this.cantidadEnStock++;
    }

    public void decrementarStock() {
        if (this.cantidadEnStock == 0) {
            throw new IllegalStateException("No se puede decrementar stock debajo de 0");
        }
        this.cantidadEnStock--;
    }
}
