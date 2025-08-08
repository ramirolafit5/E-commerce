/* package com.fl.ecommerce.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "categorias")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la categoría no puede estar vacío.")
    @Column(nullable = false, unique = true)
    private String nombre;

    // Se agrega la relación OneToMany para los productos.
    // Una categoría puede tener muchos productos.
    // El 'mappedBy' indica que la relación es gestionada por el campo 'categoria' en la clase Product.
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<Product> productos;
} */
