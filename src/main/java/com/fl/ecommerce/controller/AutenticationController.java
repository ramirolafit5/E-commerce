package com.fl.ecommerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fl.ecommerce.dto.AutenticationRequestDTO;
import com.fl.ecommerce.dto.RegisterUserDTO;
import com.fl.ecommerce.dto.UserTokenDTO;
import com.fl.ecommerce.handler.UserAlreadyExist;
import com.fl.ecommerce.service.UserService;

import jakarta.validation.Valid;

/**
 * Controlador para la gestión de autenticación y registro de usuarios.
 */
@RestController
@RequestMapping("/api/autenticacion")
public class AutenticationController {
    
    private final UserService userService;

    public AutenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registro")
    public ResponseEntity<String> registrarUsuario(@Valid @RequestBody RegisterUserDTO registroDTO) {
        try {
            userService.registrarUsuario(registroDTO); // Usar el método del UserService
            return new ResponseEntity<>("Usuario registrado exitosamente!", HttpStatus.CREATED);
        } catch (UserAlreadyExist e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenDTO> autenticarUsuario(@Valid @RequestBody AutenticationRequestDTO autenticacionDTO) {
        UserTokenDTO respuesta = userService.autenticarYGenerarToken(autenticacionDTO);
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

}
