package com.fl.ecommerce.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.fl.ecommerce.handler.NonExistentUser;
import com.fl.ecommerce.model.User;
import com.fl.ecommerce.repository.UserRepository;

@Component
public class AuthUtil {

    private final UserRepository userRepository;

    public AuthUtil (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /* 
     * Obtenemos el username del usuario autenticado
     */
    public User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByNombreUsuario(username)
            .orElseThrow(() -> new NonExistentUser("Usuario no encontrado"));
    }
}
