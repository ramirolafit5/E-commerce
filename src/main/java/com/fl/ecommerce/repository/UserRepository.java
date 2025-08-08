package com.fl.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fl.ecommerce.model.User;

/**
 * Repositorio para la entidad Usuario, permitiendo operaciones CRUD.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNombreUsuario(String nombreUsuario);

    boolean existsByNombreUsuario(String nombreUsuario);
}
