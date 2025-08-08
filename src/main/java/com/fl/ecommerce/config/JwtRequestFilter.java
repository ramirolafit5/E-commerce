package com.fl.ecommerce.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fl.ecommerce.util.JwtUtil;

import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtRequestFilter(JwtUtil jwtUtil, @Lazy UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Obtener el encabezado de autorización de la petición
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = extractJwtFromHeader(authorizationHeader);

        if (jwt != null) {
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (io.jsonwebtoken.ExpiredJwtException ex) {
                log.warn("Token JWT expirado: {}", ex.getMessage());
            } catch (SignatureException ex) {
                log.warn("Firma del token JWT inválida: {}", ex.getMessage());
            } catch (Exception ex) {
                log.error("Error inesperado al procesar el token JWT: {}", ex.getMessage());
            }
        }

        // 3. Si el nombre de usuario existe y no hay una autenticación en el contexto de seguridad
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // 4. Validar el token y autenticar al usuario
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 5. Establecer la autenticación en el contexto de seguridad de Spring
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (UsernameNotFoundException ex) {
                log.warn("Usuario extraído del token no encontrado: {}", ex.getMessage());
            }
        }

        // 6. Pasar la petición al siguiente filtro en la cadena
        filterChain.doFilter(request, response);
    }

    // Método privado para extraer el JWT del header Authorization
    private String extractJwtFromHeader(String header) {
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            return header.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}

