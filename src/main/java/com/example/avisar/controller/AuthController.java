package com.example.avisar.controller;

import com.example.avisar.dto.RegisterDTO;
import com.example.avisar.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO dados) {
        try {
            authService.registrarUsuario(dados);
            return ResponseEntity.ok("Usuário registrado com sucesso!");
        } catch (RuntimeException e) {
            // Retorna erro 400 (Bad Request) com a mensagem "Limite atingido"
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}