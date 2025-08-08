package com.fl.ecommerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.fl.ecommerce.dto.CreateProductDTO;
import com.fl.ecommerce.dto.ProductResponseDTO;
import com.fl.ecommerce.model.Product;

/**
 * Interfaz de mapeo para la entidad Producto y sus DTOs.
 * Usa MapStruct para generar la implementación durante la compilación.
 * El componente de mapeo es "spring" para permitir la inyección de dependencias.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    /* 
     * Mapea un CreateProductDTO a una entidad Product, ignorando campos como id, 
     * creador y stock que se generan automáticamente
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cantidadEnStock", ignore = true)
    @Mapping(target = "creador", ignore = true)
    Product toEntity(CreateProductDTO dto);

    /**
     * Proceso inverso. Convierte un Product en ProductResponseDTO, 
     * incluyendo el nombre de usuario del creador como 'creadorUsername'.
     */
    @Mapping(source = "creador.nombreUsuario", target = "creadorUsername")
    ProductResponseDTO toDto(Product entity);

    /**
     * Actualiza una entidad Producto existente con los datos de un ProductoActualizacionDTO.
     * Los campos nulos en el DTO no sobrescribirán los valores existentes en la entidad.
     * @param dto El DTO de actualización del producto.
     * @param entity La entidad Producto a actualizar.
     */
/*     @Mapping(target = "id", ignore = true) // Ignorar el ID en la actualización
    void updateEntityFromDto(UpdateProductDTO dto, @MappingTarget Product entity); */
}
