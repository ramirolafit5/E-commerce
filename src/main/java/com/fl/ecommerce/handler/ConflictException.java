package com.fl.ecommerce.handler;

// 409 Conflicto - Se usa cuando la solicitud no se puede completar por un conflicto con el estado actual del recurso.
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
