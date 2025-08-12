package com.fl.ecommerce.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fl.ecommerce.dto.AddProductToOrderDTO;
import com.fl.ecommerce.dto.OrderDTO;
import com.fl.ecommerce.handler.AccessDeniedException;
import com.fl.ecommerce.handler.ResourceNotFoundException;
import com.fl.ecommerce.mapper.OrderMapper;
import com.fl.ecommerce.model.Order;
import com.fl.ecommerce.model.Product;
import com.fl.ecommerce.model.User;
import com.fl.ecommerce.model.enums.OrderStatus;
import com.fl.ecommerce.repository.OrderRepository;
import com.fl.ecommerce.repository.ProductRepository;
import com.fl.ecommerce.util.AuthUtil;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final AuthUtil authUtil;

    /**
     * Constructor para la inyección de dependencias.
     */
    public OrderService(OrderRepository orderRepository ,ProductRepository productRepository, OrderMapper orderMapper, AuthUtil authUtil) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
        this.authUtil = authUtil;
    }

    @Transactional
    public OrderDTO crearPedido() {
        User usuarioAutenticado = authUtil.getAuthenticatedUser();

        Optional<Order> pedidoPendiente = orderRepository.findByUsuarioAndEstadoPedido(usuarioAutenticado, OrderStatus.PENDIENTE);

        if (pedidoPendiente.isPresent()) {
            return orderMapper.toDto(pedidoPendiente.get());
        }

        // Crear nuevo pedido si no existe
        Order nuevoPedido = new Order();
        nuevoPedido.setUsuario(usuarioAutenticado);
        nuevoPedido.setFechaPedido(LocalDateTime.now());
        nuevoPedido.setEstadoPedido(OrderStatus.PENDIENTE);
        nuevoPedido.setTotal(BigDecimal.ZERO);

        Order pedidoGuardado = orderRepository.save(nuevoPedido);

        return orderMapper.toDto(pedidoGuardado);
    }

    @Transactional
    public void eliminarPedido(Long pedidoId) {
        User usuarioAutenticado = authUtil.getAuthenticatedUser();

        Order pedido = orderRepository.findById(pedidoId)
            .filter(p -> p.getUsuario().getId().equals(usuarioAutenticado.getId()))
            .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado o no autorizado"));

        if (pedido.getEstadoPedido() != OrderStatus.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden eliminar pedidos pendientes");
        }

        orderRepository.delete(pedido);
    }

    @Transactional
    public OrderDTO agregarProductoAlPedido(AddProductToOrderDTO dto) {
        User usuarioAutenticado = authUtil.getAuthenticatedUser();

        // Buscar pedido por id y verificar que pertenece al usuario autenticado
        Order pedido = orderRepository.findById(dto.getPedidoId())
            .filter(p -> p.getUsuario().getId().equals(usuarioAutenticado.getId()))
            .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado o no autorizado"));

        if (pedido.getEstadoPedido() != OrderStatus.PENDIENTE) {
            throw new IllegalStateException("No se puede modificar un pedido cerrado");
        }

        Product producto = productRepository.findById(dto.getProductoId())
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        // Si es necesario validar que el producto pertenece al usuario autenticado:
        if (!producto.getCreador().getId().equals(usuarioAutenticado.getId())) {
            throw new AccessDeniedException("No tenés permiso para usar este producto");
        }

        pedido.addProduct(producto, dto.getCantidad());

        orderRepository.save(pedido);

        return orderMapper.toDto(pedido);
    }

    @Transactional
    public OrderDTO eliminarProductoDelPedido(Long pedidoId, Long productoId) {
        Order pedido = orderRepository.findById(pedidoId)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));

        Product producto = productRepository.findById(productoId)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        pedido.removeProduct(producto);
        orderRepository.save(pedido);

        return orderMapper.toDto(pedido);
    }
    
}
