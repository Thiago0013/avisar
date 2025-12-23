package com.example.avisar.infra.security;

import com.example.avisar.model.Usuario;
import com.example.avisar.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    final UsuarioRepository usuarioRepository;

    public SecurityFilter(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Ignora rotas públicas pra não poluir o log
        String path = request.getRequestURI();
        if (path.equals("/") || path.startsWith("/primeiravez") || path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("🔎 RECEBI UMA REQUISIÇÃO EM: " + path);

        var token = this.recoverToken(request);

        if (token != null) {
            System.out.println("   🔑 Token encontrado no pedido: '" + token + "'");

            Optional<Usuario> userOptional = usuarioRepository.findByToken(token);

            if (userOptional.isPresent()) {
                Usuario user = userOptional.get();
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var tokenNaUrl = request.getParameter("acesso");
        if (tokenNaUrl != null && !tokenNaUrl.isBlank()) return tokenNaUrl.trim();

        var authHeader = request.getHeader("Authorization");
        if (authHeader != null) return authHeader.replace("Bearer ", "").trim();

        return null;
    }
}