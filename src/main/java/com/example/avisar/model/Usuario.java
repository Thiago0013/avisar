package com.example.avisar.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios") // Boa prática: nomeia a tabela no banco
@Getter                 // Gera Getters para tudo
@Setter                 // Gera Setters para tudo
@NoArgsConstructor      // Gera construtor vazio ()
@AllArgsConstructor     // Gera construtor cheio (token, nome, data)
@EqualsAndHashCode(of = "token") // Compara objetos usando apenas o ID (token)
public class Usuario implements UserDetails {

    @Id
    @Column(length = 50) // Opcional: define tamanho maximo do token
    private String token;

    @Column(unique = true, nullable = false) // Não permite nome repetido nem nulo
    private String nome;

    private LocalDateTime dataExpiracaoVibracao;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        // Como não temos senha separada, o token age como senha
        return this.token;
    }

    @Override
    public String getUsername() {
        // O "login" do usuário é o token dele
        return this.token;
    }

    // --- Métodos de verificação de conta (Padrão: tudo true) ---

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}