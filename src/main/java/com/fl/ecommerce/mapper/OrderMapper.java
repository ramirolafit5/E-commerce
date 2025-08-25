package com.fl.ecommerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.fl.ecommerce.dto.OrderAndDetailsResponseDTO;
import com.fl.ecommerce.dto.OrderDTO;
import com.fl.ecommerce.dto.OrderDetailResponse;
import com.fl.ecommerce.model.Order;
import com.fl.ecommerce.model.OrderDetail;
import com.fl.ecommerce.model.Product;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "usuario.nombreUsuario", target = "usuarioNombre")
    OrderDTO toDto(Order entity);

    // Mapea el pedido principal
    @Mapping(source = "usuario.nombreUsuario", target = "usuarioNombre")
    @Mapping(source = "estadoPedido", target = "estadoPedido") // lo convierte a String automáticamente
    OrderAndDetailsResponseDTO orderToDto(Order entity);

    // Mapea los detalles
    @Mapping(source = "producto", target = "producto")
    OrderDetailResponse orderDetailToDto(OrderDetail detail);

    // Método auxiliar: convierte Product -> String (su nombre)
    default String map(Product producto) {
        return producto != null ? producto.getNombre() : null;
    }
}
