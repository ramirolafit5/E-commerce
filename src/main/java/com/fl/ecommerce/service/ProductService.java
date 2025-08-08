package com.fl.ecommerce.service;

import org.springframework.stereotype.Service;

import com.fl.ecommerce.dto.CreateProductDTO;
import com.fl.ecommerce.dto.ProductResponseDTO;
import com.fl.ecommerce.handler.ProductAlreadyExist;
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
            throw new ProductAlreadyExist("El producto ya fue creado por este usuario.");
        }

        Product producto = productMapper.toEntity(dto);
        // Asignar el creador
        producto.setCreador(creador);

        // Guardar y mapear respuesta
        Product productoGuardado = productRepository.save(producto);
        return productMapper.toDto(productoGuardado);
    }

    /**
     * Obtiene un producto por su ID.
     * @param id ID del producto.
     * @return El DTO del producto encontrado.
     * @throws RecursoNoEncontradoException Si el producto no es encontrado.
     */
/*     @Transactional(readOnly = true) // Solo lectura, no requiere bloqueo
    public ProductoRespuestaDTO obtenerProductoPorId(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto", "ID", id));
        return productMapper.toDto(product);
    } */

    /**
     * Obtiene todos los productos de forma paginada.
     * @param pageable Objeto que contiene la información de paginación y ordenamiento.
     * @return Una página de DTOs de productos.
     */
/*     @Transactional(readOnly = true)
    public Page<ProductoRespuestaDTO> obtenerTodosLosProductos(Pageable pageable) {
        // El repositorio ya devuelve una Page, solo necesitamos mapear su contenido.
        return productoRepository.findAll(pageable)
                .map(productoMapper::toDto);
    } */

    /**
     * Actualiza un producto existente.
     * @param id ID del producto a actualizar.
     * @param dto Datos para la actualización del producto.
     * @return El DTO del producto actualizado.
     * @throws RecursoNoEncontradoException Si el producto no es encontrado.
     */
/*     @Transactional
    public ProductoRespuestaDTO actualizarProducto(Long id, ProductoActualizacionDTO dto) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto", "ID", id));

        // Aplica solo los campos no nulos del DTO a la entidad existente
        productoMapper.updateEntityFromDto(dto, productoExistente);

        Producto productoActualizado = productoRepository.save(productoExistente);
        return productoMapper.toDto(productoActualizado);
    } */

    /**
     * Elimina un producto por su ID.
     * @param id ID del producto a eliminar.
     * @throws RecursoNoEncontradoException Si el producto no es encontrado.
     */
/*     @Transactional
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Producto", "ID", id);
        }
        productoRepository.deleteById(id);
    } */
}
