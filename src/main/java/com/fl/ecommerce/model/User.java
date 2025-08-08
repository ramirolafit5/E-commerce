package com.fl.ecommerce.model;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fl.ecommerce.model.enums.Rol;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Entity
@Table(name = "usuarios")
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de usuario no puede estar vacío.")
    @Column(unique = true, nullable = false)
    private String nombreUsuario;

    // Se almacena el hash de la contraseña, no el texto plano, por seguridad.
    @NotBlank(message = "La contraseña no puede estar vacía.")
    @Column(nullable = false)
    private String password;

    // Relación con un tipo básico (enum). Se guarda en una tabla separada 'usuario_roles'.
    @ElementCollection(targetClass = Rol.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "id_usuario"))
    @Enumerated(EnumType.STRING) // Guarda el nombre del enum (ADMIN, USER) en la DB.
    @Column(name = "rol", nullable = false)
    private List<Rol> roles;

    // Relación Uno a Muchos: Un usuario puede tener muchos pedidos.
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> pedidos;


    // --- Métodos de la interfaz UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convierte los roles del usuario a una lista de GrantedAuthority.
        // Se usa .name() para obtener el nombre del enum.
        return this.roles.stream()
                         .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.name()))
                         .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        // Devuelve el hash de la contraseña.
        return this.password;
    }

    @Override
    public String getUsername() {
        // Devuelve el nombre de usuario para la autenticación.
        return this.nombreUsuario;
    }
}
