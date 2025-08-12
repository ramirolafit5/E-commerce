package com.fl.ecommerce.handler;

// 401 Unauthorized - Se usa cuando el usuario no está autenticado o sus credenciales son inválidas.
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
