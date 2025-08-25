package com.fl.ecommerce.dto;

import java.util.List;

import com.fl.ecommerce.model.enums.Rol;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String nombreUsuario;
    private List<Rol> roles;
}
