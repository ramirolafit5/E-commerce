package com.fl.ecommerce.handler;

public class UserAlreadyExist extends RuntimeException{
        public UserAlreadyExist(String message) {
        super(message);
    }
}
