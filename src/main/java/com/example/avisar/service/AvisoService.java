package com.example.avisar.service;

import com.example.avisar.model.Usuario;
import com.example.avisar.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AvisoService {

    final UsuarioRepository usuarioRepository;

    public AvisoService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public void disparar(String nomeRemetente) {

        List<Usuario> todos = usuarioRepository.findAll();

        for (Usuario destino : todos) {
            if (destino.getNome().equals(nomeRemetente)) {
                destino.setDataExpiracaoVibracao(LocalDateTime.now().plusMinutes(1));
                usuarioRepository.save(destino);

                System.out.println("💘 SINAL ENVIADO: De " + nomeRemetente + " Para " + destino.getNome());
            }
        }
    }

    public boolean status(String nome) {
        var usuarioOptional = usuarioRepository.findByNome(nome);

        if (usuarioOptional.isEmpty()) {
            return false;
        }

        Usuario usuario = usuarioOptional.get();
        LocalDateTime validade = usuario.getDataExpiracaoVibracao();

        System.out.println(validade);

        if (validade == null) {
            return false;
        }

        return LocalDateTime.now().isBefore(validade);
    }
}