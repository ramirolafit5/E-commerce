package com.fl.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fl.ecommerce.model.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>{
    
}
