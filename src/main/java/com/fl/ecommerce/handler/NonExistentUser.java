package com.fl.ecommerce.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED) // Esto hace que se devuelva 401
public class NonExistentUser extends RuntimeException {
    public NonExistentUser(String mensaje) {
        super(mensaje);
    }
}
