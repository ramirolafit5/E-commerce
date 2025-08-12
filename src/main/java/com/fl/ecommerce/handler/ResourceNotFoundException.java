package com.fl.ecommerce.handler;

// 404 No encontrado - Se usa cuando el recurso solicitado no existe o no se encontró en el sistema.
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
