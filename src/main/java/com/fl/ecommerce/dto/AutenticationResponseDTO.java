package com.fl.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta de autenticación (contiene el token JWT, si se implementara).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutenticationResponseDTO {
    private String mensaje;
    private String token; // Podría ser un token JWT si se implementara seguridad más avanzada
}
