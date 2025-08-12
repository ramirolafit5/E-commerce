package com.fl.ecommerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.fl.ecommerce.dto.OrderDTO;
import com.fl.ecommerce.model.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "usuario.nombreUsuario", target = "usuarioNombre")
    OrderDTO toDto(Order entity);
}
