package com.fl.ecommerce.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data               // Genera getters, setters, toString, equals, hashCode
@Builder            // Para crear instancias con builder (fluido y limpio)
@NoArgsConstructor  // Constructor vacío
@AllArgsConstructor // Constructor con todos los campos
public class UserTokenDTO {
    private Long id;
    private String username;
    private List<String> roles;  // Roles como strings, p. ej: ["ROLE_ADMIN", "ROLE_USER"]
    private String token;  // <-- agregá este campo para guardar el JWT
}
