package com.fl.ecommerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fl.ecommerce.dto.AgregarStockRequestDTO;
import com.fl.ecommerce.dto.CreateProductDTO;
import com.fl.ecommerce.dto.ProductResponseDTO;
import com.fl.ecommerce.dto.UpdateProductDTO;
import com.fl.ecommerce.service.ProductService;

import jakarta.validation.Valid;

/**
 * Controlador REST para la gestión de productos.
 * Los endpoints están protegidos por roles de usuario.
 */
@RestController
@RequestMapping("/api/productos")
public class ProductController {

    //Inyeccion de dependencias
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //Crea un nuevo producto. Recibe por parametro un DTO y devuelve otro DTO diferente.
    @PostMapping("/crear")
    public ResponseEntity<ProductResponseDTO> crearProducto(@Valid @RequestBody CreateProductDTO productoCreacionDTO) {
        ProductResponseDTO nuevoProducto = productService.crearProducto(productoCreacionDTO);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    //Obtener todos los productos del usuario autenticado.
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getProducts() {
        List<ProductResponseDTO> productos = productService.getAllProductsFromCurrentUser();
        return ResponseEntity.ok(productos); // HTTP 200
    }

    //Actualizar un producto por su id.
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ProductResponseDTO> actualizarProducto(@PathVariable Long id, @Valid @RequestBody UpdateProductDTO productUpdated) {
        ProductResponseDTO productoActualizado = productService.updateProducto(id, productUpdated);
        return ResponseEntity.ok(productoActualizado); 
    }

    //Eliminamos un producto por su id.
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productService.eliminarProducto(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/agregar-stock")
    public ResponseEntity<ProductResponseDTO> agregarStock(@Valid @RequestBody AgregarStockRequestDTO dto) {
        ProductResponseDTO productoActualizado = productService.agregarStockAProducto(dto);
        return ResponseEntity.ok(productoActualizado);
    }

    @PostMapping("/incrementaren1/{id}")
    public ResponseEntity<ProductResponseDTO> incrementarStockEn1(@PathVariable Long id) {
        ProductResponseDTO stockActualizado = productService.incrementarStock(id);
        return ResponseEntity.ok(stockActualizado);
    }

    @PostMapping("/decrementaren1/{id}")
    public ResponseEntity<ProductResponseDTO> decrementarStockEn1(@PathVariable Long id) {
        ProductResponseDTO stockActualizado = productService.decrementarStock(id);
        return ResponseEntity.ok(stockActualizado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> obtenerProductoPorId(@PathVariable Long id) {
        ProductResponseDTO producto = productService.getProductById(id);
        return new ResponseEntity<>(producto, HttpStatus.OK);
    }
}
