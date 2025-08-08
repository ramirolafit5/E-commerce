package com.fl.ecommerce.handler;

public class ProductAlreadyExist extends RuntimeException{
    public ProductAlreadyExist(String message) {
        super(message);
    }
}
