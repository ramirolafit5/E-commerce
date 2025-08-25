package com.fl.ecommerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fl.ecommerce.dto.AddProductToOrderDTO;
import com.fl.ecommerce.dto.OrderAndDetailsResponseDTO;
import com.fl.ecommerce.dto.OrderDTO;
import com.fl.ecommerce.service.OrderService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/pedidos")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService ){
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderAndDetailsResponseDTO> getMethodName(@PathVariable Long id) {
        OrderAndDetailsResponseDTO detallesDelPedido = orderService.obtenerPedidoConDetalles(id);
        return ResponseEntity.ok(detallesDelPedido);
    }

    @PostMapping("/crear")
    public ResponseEntity<OrderDTO> crearPedido() {
        OrderDTO nuevoPedido = orderService.crearPedido();
        return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
    }

    @DeleteMapping("/{pedidoId}")
    public ResponseEntity<Void> cancelarPedido(@PathVariable Long pedidoId) {
        orderService.cancelarPedido(pedidoId);
        return ResponseEntity.noContent().build(); //hace lo mismo que return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/agregar-producto")
    public ResponseEntity<OrderDTO> agregarProducto(@Valid @RequestBody AddProductToOrderDTO dto) {
        OrderDTO updatedOrder = orderService.agregarProductoAlPedido(dto);
        return ResponseEntity.ok(updatedOrder);
    }

    @PostMapping("/{pedidoId}/quitar-producto/{productoId}")
    public ResponseEntity<OrderDTO> quitarProducto(@PathVariable Long pedidoId, @PathVariable Long productoId) {
        OrderDTO updatedOrder = orderService.quitarProductoAlPedido(pedidoId, productoId);
        return ResponseEntity.ok(updatedOrder);
    }

    @PostMapping("/anular-pedido/{pedidoId}")
    public ResponseEntity<String> anularPedido(@PathVariable Long pedidoId) {
        orderService.anularPedido(pedidoId);
        return new ResponseEntity<>("Pedido anulado exitosamente!", HttpStatus.OK);
    }

    @PostMapping("/confirmar-pedido/{pedidoId}")
    public ResponseEntity<String> confirmarPedido(@PathVariable Long pedidoId) {
        orderService.confirmarPedido(pedidoId);
        return new ResponseEntity<>("Pedido confirmado exitosamente!", HttpStatus.CREATED);
    }

}
