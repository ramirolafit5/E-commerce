package com.fl.ecommerce.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

/**
 * Clase de configuración de seguridad para la aplicación Spring Boot.
 * Habilita la seguridad web y la seguridad a nivel de método.
 */
@Configuration
@EnableWebSecurity // Habilita la seguridad web
@EnableMethodSecurity // Habilita la seguridad a nivel de método (ej. @PreAuthorize)
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    /**
     * Define el codificador de contraseñas (BCryptPasswordEncoder).
     * @return Una instancia de PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura el AuthenticationManager, que se encarga de la autenticación de usuarios.
     * Utiliza un DaoAuthenticationProvider con nuestro ServicioDetallesUsuario y PasswordEncoder.
     * @return Una instancia de AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Define la cadena de filtros de seguridad HTTP.
     * Configura la autorización para diferentes endpoints y habilita la autenticación básica.
     * @param http Objeto HttpSecurity para configurar la seguridad.
     * @return Una instancia de SecurityFilterChain.
     * @throws Exception Si ocurre un error de configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of("http://localhost:3000")); // Aqui va la URL del frontend
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            config.setAllowedHeaders(List.of("*"));
            config.setAllowCredentials(true);
            return config;
            }))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(HttpMethod.POST, "/api/autenticacion/registro").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/autenticacion/login").permitAll()
                    .requestMatchers(HttpMethod.POST,"/api/productos/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/pedidos").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/pedidos/usuario/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/pedidos/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Añade tu filtro JWT antes del filtro de autenticación de Spring
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
