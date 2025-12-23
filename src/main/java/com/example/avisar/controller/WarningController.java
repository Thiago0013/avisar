package com.example.avisar.controller;


import com.example.avisar.repository.UsuarioRepository;
import com.example.avisar.service.AvisoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WarningController {

    AvisoService avisoService;
    UsuarioRepository usuarioRepository;

    public WarningController(AvisoService avisoService, UsuarioRepository usuarioRepository){
        this.avisoService = avisoService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("""
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
            <meta charset="UTF-8">
            <title>Controle</title>
            <style>
                body {
                    margin: 0;
                    background-color: #121212;
                    display: flex;
                    flex-direction: column;
                    justify-content: center;
                    align-items: center;
                    height: 100vh;
                    font-family: 'Segoe UI', sans-serif;
                    overflow: hidden;
                    user-select: none;
                }
                
                h2 { color: rgba(255, 255, 255, 0.9); margin-bottom: 30px; letter-spacing: 2px; text-transform: uppercase; }
                
                #bigButton {
                    width: 220px; height: 220px; border-radius: 50%; border: none;
                    background: radial-gradient(circle at 30% 30%, #ff4d4d, #cc0000);
                    box-shadow: 0 0 20px rgba(204, 0, 0, 0.4); cursor: pointer;
                    position: relative; outline: none; -webkit-tap-highlight-color: transparent;
                    transition: transform 0.1s;
                }
                #bigButton:active { transform: scale(0.95); }
                #bigButton::after {
                    content: '❤️'; font-size: 80px; position: absolute;
                    top: 50%; left: 50%; transform: translate(-50%, -50%);
                }
                
                .status { margin-top: 30px; color: #888; font-size: 16px; font-weight: bold; }
                
                /* O MODO ESPIÃO */
                .debug-info {
                    position: absolute; bottom: 10px; 
                    color: #444; font-size: 12px; font-family: monospace;
                }
            </style>
        </head>
        <body>
            <h2>Alertar</h2>
            <button id="bigButton" onclick="enviarSinal()"></button>
            <div class="status" id="statusText">Toque para chamar</div>
            
            <div class="debug-info" id="debug">Carregando...</div>

            <script>
                // Pega a senha do navegador
                const token = localStorage.getItem('vibrator_token');
                
                // Mostra na tela pra gente conferir (Modo Espião)
                document.getElementById('debug').innerText = 'Token atual: ' + (token ? token : 'NENHUM');

                function enviarSinal() {
                    const texto = document.getElementById('statusText');
                    
                    if (!token) {
                        texto.innerHTML = "❌ Sem token configurado";
                        texto.style.color = "orange";
                        return;
                    }

                    texto.innerHTML = "Enviando...";
                    texto.style.color = "#ff4d4d";

                    // Envia a senha na URL
                    fetch(`/trigger?acesso=${token}`, { method: 'POST' })
                        .then(res => {
                            if(res.ok) {
                                texto.innerHTML = "Sinal Enviado! ✨";
                                texto.style.color = "#00cc66";
                                setTimeout(() => { texto.innerHTML = "Toque para chamar"; texto.style.color = "#888"; }, 2000);
                            } else {
                                texto.innerHTML = "🚫 Token Inválido (" + res.status + ")";
                                texto.style.color = "red";
                            }
                        })
                        .catch(err => {
                            texto.innerHTML = "Erro de conexão ❌";
                            texto.style.color = "red";
                        });
                }
            </script>
        </body>
        </html>
    """);
    }


    @GetMapping("/primeiravez/{token}")
    public ResponseEntity<String> setupInicial(@PathVariable String token) {

        return ResponseEntity.ok(String.format("""
        <!DOCTYPE html>
        <html>
        <head>
            <title>Configurando...</title>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <style>
                body { 
                    background-color: #121212; 
                    color: white; 
                    font-family: sans-serif; 
                    display: flex; 
                    justify-content: center; 
                    align-items: center; 
                    height: 100vh;
                    text-align: center;
                }
            </style>
        </head>
        <body>
            <div>
                <h1>Configurando acesso... 🔐</h1>
                <p>Aguarde um instante.</p>
            </div>

            <script>
                // 1. Salva o token que veio na URL dentro do navegador
                localStorage.setItem('vibrator_token', '%s');

                // 2. Aguarda 1.5 segundos (pra dar tempo de ler) e redireciona pro Botão
                setTimeout(() => {
                    window.location.href = '/';
                }, 1500);
            </script>
        </body>
        </html>
    """, token)); // O %s ali em cima é substituído pelo 'token' aqui
    }


    @PostMapping("/trigger")
    public void trigger(){
        avisoService.disparar("Ian");
    }

    @GetMapping("/status")
    public boolean status(){
        return avisoService.status("Ian");
    }
}
