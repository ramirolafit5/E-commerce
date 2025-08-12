package com.fl.ecommerce.handler;

// 500 Error interno - Se usa para errores inesperados o fallos internos del servidor que no están contemplados.
public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String message) {
        super(message);
    }
}
