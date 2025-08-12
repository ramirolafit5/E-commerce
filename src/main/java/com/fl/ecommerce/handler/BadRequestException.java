package com.fl.ecommerce.handler;

// 400 Bad Request - Se usa cuando el cliente envía una solicitud inválida o mal formada.
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
