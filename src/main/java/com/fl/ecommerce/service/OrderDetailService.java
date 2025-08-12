package com.fl.ecommerce.service;

import org.springframework.stereotype.Service;

import com.fl.ecommerce.mapper.OrderMapper;
import com.fl.ecommerce.repository.OrderDetailRepository;
import com.fl.ecommerce.repository.OrderRepository;
import com.fl.ecommerce.repository.ProductRepository;
import com.fl.ecommerce.util.AuthUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderDetailService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderMapper orderMapper;
    private final AuthUtil authUtil;

    
}
