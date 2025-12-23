package com.example.avisar.repository;

import com.example.avisar.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    public Optional<Usuario> findByToken(String token);

    public Optional<Usuario> findByNome(String nome);
}
