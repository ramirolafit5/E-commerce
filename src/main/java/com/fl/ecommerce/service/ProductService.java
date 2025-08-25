package com.fl.ecommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fl.ecommerce.dto.AgregarStockRequestDTO;
import com.fl.ecommerce.dto.CreateProductDTO;
import com.fl.ecommerce.dto.ProductResponseDTO;
import com.fl.ecommerce.dto.UpdateProductDTO;
import com.fl.ecommerce.handler.AccessDeniedException;
import com.fl.ecommerce.handler.ConflictException;
import com.fl.ecommerce.handler.ResourceNotFoundException;
import com.fl.ecommerce.mapper.ProductMapper;
import com.fl.ecommerce.model.Product;
import com.fl.ecommerce.model.User;
import com.fl.ecommerce.repository.ProductRepository;
import com.fl.ecommerce.util.AuthUtil;

import jakarta.transaction.Transactional;

/**
 * Servicio para la gestión de productos, conteniendo la lógica de negocio.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final AuthUtil authUtil;

    /**
     * Constructor para la inyección de dependencias.
     * @param productoRepository Repositorio de productos.
     * @param productoMapper Mapeador de productos.
     */
    public ProductService(ProductRepository productRepository, ProductMapper productMapper, AuthUtil authUtil) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.authUtil = authUtil;
    }

    /**
     * Crea un nuevo producto en la base de datos.
     * @param dto Datos para la creación del producto.
     * @return El DTO del producto creado.
     */
    @Transactional
    public ProductResponseDTO crearProducto(CreateProductDTO dto) {
        User creador = authUtil.getAuthenticatedUser();
        
        if (productRepository.existsByNombreAndCreador(dto.getNombre(), creador)) {
            throw new ConflictException("El producto ya fue creado por este usuario.");
        }

        Product producto = productMapper.toEntity(dto);
        // Asignar el creador
        producto.setCreador(creador);

        // Guardar y mapear respuesta
        Product productoGuardado = productRepository.save(producto);
        return productMapper.toDto(productoGuardado);
    }

    public List<ProductResponseDTO> getAllProductsFromCurrentUser(){
        User authUser = authUtil.getAuthenticatedUser();

        List<Product> productos = productRepository.findByCreadorOrderByIdAsc(authUser);

        return productos.stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Transactional
    public ProductResponseDTO updateProducto(Long id, UpdateProductDTO dto) {
        User authUser = authUtil.getAuthenticatedUser();

        // Validar que exista el producto
        Product productoExistente = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        // Validar que el usuario autenticado sea el creador
        if (!productoExistente.getCreador().getId().equals(authUser.getId())) {
            throw new AccessDeniedException("No tenés permiso para actualizar este producto.");
        }

        // Usar el mapper para actualizar solo los campos permitidos
        productMapper.updateEntityFromDto(dto, productoExistente);

        // Guardar los cambios
        Product productoActualizado = productRepository.save(productoExistente);

        // Mapear a DTO para respuesta
        return productMapper.toDto(productoActualizado);
    }

    @Transactional
    public void eliminarProducto(Long id) {
        User authUser = authUtil.getAuthenticatedUser();

        // Validar que exista el producto
        Product productoExistente = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        // Validar que el usuario autenticado sea el creador
        if (!productoExistente.getCreador().getId().equals(authUser.getId())) {
            throw new AccessDeniedException("No tenés permiso para actualizar este producto.");
        }
        productRepository.deleteById(id);
    }

    @Transactional
    public ProductResponseDTO agregarStockAProducto(AgregarStockRequestDTO dto) {
        User usuarioAutenticado = authUtil.getAuthenticatedUser();

        Product producto = productRepository.findById(dto.getProductoId())
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        if (!producto.getCreador().getId().equals(usuarioAutenticado.getId())) {
            throw new AccessDeniedException("No tenés permiso para usar este producto");
        }

        producto.setCantidadEnStock(producto.getCantidadEnStock() + dto.getCantidad());
        Product productoActualizado = productRepository.save(producto);
        return productMapper.toDto(productoActualizado);
    }

    public ProductResponseDTO incrementarStock(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        product.incrementarStock();
        Product stockActualizado = productRepository.save(product);
        return productMapper.toDto(stockActualizado);
    }

    public ProductResponseDTO decrementarStock(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        product.decrementarStock();
        Product stockActualizado = productRepository.save(product);
        return productMapper.toDto(stockActualizado);
    }

    public ProductResponseDTO getProductById(Long productId) {
        User authUser = authUtil.getAuthenticatedUser();

        Product product = productRepository.findByIdAndCreador(productId, authUser)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        return productMapper.toDto(product);
    }

}
