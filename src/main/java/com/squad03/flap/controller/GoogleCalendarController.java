package com.squad03.flap.controller;

import com.squad03.flap.model.Usuario;
import com.squad03.flap.repository.UsuarioRepository;
import com.squad03.flap.service.GoogleCalendarService;
import com.squad03.flap.util.SegurancaUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@RestController
@RequestMapping("/google")
@CrossOrigin(origins = "*")
@Tag(name = "Google Calendar", description = "API para integração com Google Calendar")
public class GoogleCalendarController {

    @Autowired
    private GoogleCalendarService googleCalendarService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SegurancaUtils segurancaUtils;

    @GetMapping("/authorize")
    @Operation(summary = "Iniciar fluxo de autorização OAuth do Google Calendar")
    public ResponseEntity<?> authorize() {
        try {
            String emailUsuario = segurancaUtils.getUsuarioLogadoEmail();
            Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            String authUrl = googleCalendarService.getAuthorizationUrl(usuario.getId().toString());

            return ResponseEntity.ok(Map.of(
                    "authUrl", authUrl,
                    "message", "Redirecione o usuário para esta URL"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Erro ao gerar URL de autorização: " + e.getMessage()
            ));
        }
    }



    @GetMapping("/callback")
    @Operation(summary = "Callback do OAuth - Recebe código de autorização do Google")
    public RedirectView callback(@RequestParam("code") String code,
                                 @RequestParam("state") String userId) {
        try {
            googleCalendarService.handleCallback(code, userId);

            // Redireciona para o frontend com sucesso
            return new RedirectView("https://flap.gabrielfiel.com.br/perfil?google=success");
        } catch (Exception e) {
            System.err.println("❌ Erro no callback do Google: " + e.getMessage());
            e.printStackTrace();

            // Redireciona para o frontend com erro
            return new RedirectView("https://flap.gabrielfiel.com.br/perfil?google=error");
        }
    }

    @GetMapping("/status")
    @Operation(summary = "Verificar se Google Calendar está conectado")
    public ResponseEntity<?> getStatus() {
        try {
            String emailUsuario = segurancaUtils.getUsuarioLogadoEmail();
            Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            return ResponseEntity.ok(Map.of(
                    "conectado", usuario.getGoogleCalendarConectado(),
                    "email", usuario.getEmail()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Erro ao verificar status: " + e.getMessage()
            ));
        }
    }

    @DeleteMapping("/disconnect")
    @Operation(summary = "Desconectar Google Calendar")
    public ResponseEntity<?> disconnect() {
        try {
            String emailUsuario = segurancaUtils.getUsuarioLogadoEmail();
            Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            googleCalendarService.desconectar(usuario);

            return ResponseEntity.ok(Map.of(
                    "message", "Google Calendar desconectado com sucesso"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Erro ao desconectar: " + e.getMessage()
            ));
        }
    }
}
