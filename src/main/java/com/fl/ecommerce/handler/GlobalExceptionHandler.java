package com.fl.ecommerce.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Desde aca comunico ProductAlreadyExist junto con el servicio
    @ExceptionHandler(ProductAlreadyExist.class)
    public ResponseEntity<Map<String, String>> handleProductAlreadyExist(ProductAlreadyExist ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);  // 409 Conflict es apropiado aquí
    }

    // Manejo genérico de errores (por ejemplo, errores inesperados)
/*     @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Ocurrió un error inesperado.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    } */
}

