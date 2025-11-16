package com.squad03.flap.controller;

import com.squad03.flap.service.NotificacaoService;
import com.squad03.flap.model.TipoNotificacao;
import com.squad03.flap.repository.UsuarioRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/test")
public class TestNotificacaoController {

    private final NotificacaoService notificacaoService;
    private final UsuarioRepository usuarioRepository;

    public TestNotificacaoController(NotificacaoService notificacaoService,
                                     UsuarioRepository usuarioRepository) {
        this.notificacaoService = notificacaoService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/notificacao")
    public String testarNotificacao(Authentication auth) {
        String email = auth.getName();
        var usuario = usuarioRepository.findByEmail(email).orElseThrow();

        notificacaoService.criarNotificacao(
                TipoNotificacao.MENCAO,
                "🧪 Teste de Notificação",
                "Esta é uma notificação de teste em tempo real! Se você está vendo isso, funcionou! 🎉",
                usuario,
                usuario,
                null
        );

        return "✅ Notificação enviada para " + email;
    }
}
