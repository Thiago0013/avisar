package com.example.avisar.service;

import com.example.avisar.dto.RegisterDTO;
import com.example.avisar.model.Usuario;
import com.example.avisar.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    final UsuarioRepository usuarioRepository;

    public AuthService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        return usuarioRepository.findByToken(token)
                .orElseThrow(() -> new UsernameNotFoundException("Token inválido ou não encontrado!"));
    }

    public void registrarUsuario(RegisterDTO dados) {
        if (usuarioRepository.count() >= 2) {
            throw new RuntimeException("Limite de usuários atingido! A casa está cheia (Max 2).");
        }

        if (usuarioRepository.findByToken(dados.token()).isPresent()) {
            throw new RuntimeException("Esse token já existe!");
        }

        if (usuarioRepository.findByNome(dados.nome()).isPresent()) {
            throw new RuntimeException("Esse nome já existe!");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(dados.nome());
        novoUsuario.setToken(dados.token());

        usuarioRepository.save(novoUsuario);
    }
}