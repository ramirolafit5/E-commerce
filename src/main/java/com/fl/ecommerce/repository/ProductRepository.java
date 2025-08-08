package com.fl.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fl.ecommerce.model.Product;
import com.fl.ecommerce.model.User;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
    boolean existsByNombreAndCreador(String nombre, User creador);

    List<Product> findByCreador(User creador);
}
