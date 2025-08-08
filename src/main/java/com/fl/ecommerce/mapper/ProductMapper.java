package com.fl.ecommerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.fl.ecommerce.dto.CreateProductDTO;
import com.fl.ecommerce.dto.ProductResponseDTO;
import com.fl.ecommerce.dto.UpdateProductDTO;
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
     * y stock que se generan automáticamente, y creador que se genera con el autenticado actual.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cantidadEnStock", ignore = true)
    @Mapping(target = "creador", ignore = true)
    Product toEntity(CreateProductDTO dto);

    /**
     * Proceso inverso. Convierte un Product en ProductResponseDTO, 
     * incluyendo el nombre de usuario del creador como 'creadorUsername'.
     */
    //@Mapping(source = "creador.nombreUsuario", target = "creadorUsername")
    ProductResponseDTO toDto(Product entity);

    // Actualizar entidad existente a partir de UpdateProductDTO
    @Mapping(target = "id", ignore = true)               // No permitimos cambiar ID
    @Mapping(target = "creador", ignore = true)          // No se modifica el creador
    @Mapping(target = "cantidadEnStock", ignore = true)  // Lo dejamos sin cambios
    void updateEntityFromDto(UpdateProductDTO dto, @MappingTarget Product entity);
}
