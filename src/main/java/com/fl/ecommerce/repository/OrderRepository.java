package com.fl.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fl.ecommerce.model.Order;
import com.fl.ecommerce.model.User;
import com.fl.ecommerce.model.enums.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long>{
    Optional<Order> findByUsuarioAndEstadoPedido(User usuario, OrderStatus estadoPedido);
}
