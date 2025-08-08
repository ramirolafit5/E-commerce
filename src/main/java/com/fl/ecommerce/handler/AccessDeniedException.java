package com.fl.ecommerce.handler;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String mensaje){
        super(mensaje);
    }
}
