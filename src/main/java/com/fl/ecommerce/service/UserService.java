package com.fl.ecommerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fl.ecommerce.dto.AutenticationRequestDTO;
import com.fl.ecommerce.dto.RegisterUserDTO;
import com.fl.ecommerce.dto.UserTokenDTO;
import com.fl.ecommerce.handler.NonExistentUser;
import com.fl.ecommerce.handler.UserAlreadyExist;
import com.fl.ecommerce.model.User;
import com.fl.ecommerce.model.enums.Rol;
import com.fl.ecommerce.repository.UserRepository;
import com.fl.ecommerce.util.JwtUtil;

import jakarta.transaction.Transactional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, 
                       @Lazy AuthenticationManager authenticationManager, 
                       JwtUtil jwtUtil, 
                       @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Metodo para autenticar y generar un token
      */
    public UserTokenDTO autenticarYGenerarToken(AutenticationRequestDTO requestDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDTO.getNombreUsuario(),
                            requestDTO.getContrasena()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new NonExistentUser("Usuario o contraseña incorrectos");
        }

        User usuario = (User) loadUserByUsername(requestDTO.getNombreUsuario());

        UserTokenDTO userTokenDTO = UserTokenDTO.builder()
            .id(usuario.getId())
            .username(usuario.getUsername())
            .roles(usuario.getRoles().stream().map(Rol::name).collect(Collectors.toList()))
            .build();

        String token = jwtUtil.generateToken(userTokenDTO);
        userTokenDTO.setToken(token);
        return userTokenDTO;
    }

    /**
     * Metodo para 
      */
    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
        User usuario = userRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + nombreUsuario));
        return usuario;
    }

    /**
     * Metodo para registrar un nuevo usuario (movido desde AuthService)
     */
    @Transactional
    public User registrarUsuario(RegisterUserDTO registroDTO) {
        if (userRepository.existsByNombreUsuario(registroDTO.getNombreUsuario())) {
            throw new UserAlreadyExist("El nombre de usuario '" + registroDTO.getNombreUsuario() + "' ya está en uso.");
        }
        User usuario = new User();
        usuario.setNombreUsuario(registroDTO.getNombreUsuario());
        usuario.setPassword(passwordEncoder.encode(registroDTO.getContrasena()));
        usuario.setRoles(List.of(registroDTO.getRol()));
        return userRepository.save(usuario);
    }
}