package com.squad03.flap.controller;

import com.squad03.flap.DTO.NotificacaoDTO;
import com.squad03.flap.service.NotificacaoService;
import com.squad03.flap.util.SegurancaUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notificacoes")
@CrossOrigin(origins = "*")
public class NotificacaoController {

    private final NotificacaoService notificacaoService;
    private final SegurancaUtils segurancaUtils;

    public NotificacaoController(NotificacaoService notificacaoService,
                                 SegurancaUtils segurancaUtils) {
        this.notificacaoService = notificacaoService;
        this.segurancaUtils = segurancaUtils;
    }

    // ===== GET /notificacoes - Buscar todas =====
    @GetMapping
    public ResponseEntity<List<NotificacaoDTO>> buscarNotificacoes() {
        String emailUsuario = segurancaUtils.getUsuarioLogadoEmail();
        return notificacaoService.buscarNotificacoes(emailUsuario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/teste")
    public ResponseEntity<String> teste() {
        return ResponseEntity.ok("Notificações funcionando!");
    }

    // ===== GET /notificacoes/nao-lidas - Buscar não lidas =====
    @GetMapping("/nao-lidas")
    public ResponseEntity<List<NotificacaoDTO>> buscarNaoLidas() {
        String emailUsuario = segurancaUtils.getUsuarioLogadoEmail();
        return notificacaoService.buscarNaoLidas(emailUsuario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ===== GET /notificacoes/contador - Contar não lidas =====
    @GetMapping("/contador")
    public ResponseEntity<Map<String, Long>> contarNaoLidas() {
        String emailUsuario = segurancaUtils.getUsuarioLogadoEmail();
        return notificacaoService.contarNaoLidas(emailUsuario)
                .map(contador -> ResponseEntity.ok(Map.of("naoLidas", contador)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ===== PATCH /notificacoes/{id}/marcar-lida - Marcar como lida =====
    @PatchMapping("/{id}/marcar-lida")
    public ResponseEntity<NotificacaoDTO> marcarComoLida(@PathVariable Long id) {
        String emailUsuario = segurancaUtils.getUsuarioLogadoEmail();
        return notificacaoService.marcarComoLida(id, emailUsuario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ===== PATCH /notificacoes/marcar-todas-lidas - Marcar todas como lidas =====
    @PatchMapping("/marcar-todas-lidas")
    public ResponseEntity<Map<String, Integer>> marcarTodasComoLidas() {
        String emailUsuario = segurancaUtils.getUsuarioLogadoEmail();
        return notificacaoService.marcarTodasComoLidas(emailUsuario)
                .map(quantidade -> ResponseEntity.ok(Map.of("marcadas", quantidade)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ===== DELETE /notificacoes/{id} - Deletar notificação =====
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        String emailUsuario = segurancaUtils.getUsuarioLogadoEmail();
        return notificacaoService.deletar(id, emailUsuario)
                .map(v -> ResponseEntity.ok().<Void>build())
                .orElse(ResponseEntity.notFound().build());
    }
}
