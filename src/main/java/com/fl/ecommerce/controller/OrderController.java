package com.fl.ecommerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fl.ecommerce.dto.AddProductToOrderDTO;
import com.fl.ecommerce.dto.OrderDTO;
import com.fl.ecommerce.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pedidos")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping("/crear")
    public ResponseEntity<OrderDTO> crearPedido() {
        OrderDTO nuevoPedido = orderService.crearPedido();
        return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
    }

    @DeleteMapping("/{pedidoId}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long pedidoId) {
        orderService.eliminarPedido(pedidoId);
        return ResponseEntity.noContent().build(); //hace lo mismo que return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/agregar-detalle")
    public ResponseEntity<OrderDTO> agregarProducto(@Valid @RequestBody AddProductToOrderDTO dto) {
        OrderDTO updatedOrder = orderService.agregarProductoAlPedido(dto);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{pedidoId}/producto/{productoId}")
    public ResponseEntity<Void> eliminarDetallePedido(@PathVariable Long pedidoId, @PathVariable Long productoId) {
        orderService.eliminarProductoDelPedido(pedidoId, productoId);
        return ResponseEntity.noContent().build(); //hace lo mismo que return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
