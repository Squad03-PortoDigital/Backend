package com.squad03.flap.service;

import com.squad03.flap.DTO.NotificacaoDTO;
import com.squad03.flap.DTO.RemetenteDTO;
import com.squad03.flap.exception.TarefaValidacaoException;
import com.squad03.flap.model.*;
import com.squad03.flap.repository.NotificacaoRepository;
import com.squad03.flap.repository.UsuarioRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificacaoService(NotificacaoRepository notificacaoRepository,
                              UsuarioRepository usuarioRepository,
                              SimpMessagingTemplate messagingTemplate) {
        this.notificacaoRepository = notificacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.messagingTemplate = messagingTemplate;
    }

    // ===== Buscar todas as notificações do usuário =====
    public Optional<List<NotificacaoDTO>> buscarNotificacoes(String emailUsuario) {
        return usuarioRepository.findByEmail(emailUsuario)
                .map(usuario -> {
                    List<Notificacao> notificacoes = notificacaoRepository
                            .findByUsuarioOrderByDataHoraDesc(usuario);

                    return notificacoes.stream()
                            .map(this::converterParaDTO)
                            .collect(Collectors.toList());
                });
    }

    // ===== Buscar não lidas =====
    public Optional<List<NotificacaoDTO>> buscarNaoLidas(String emailUsuario) {
        return usuarioRepository.findByEmail(emailUsuario)
                .map(usuario -> {
                    List<Notificacao> notificacoes = notificacaoRepository
                            .findByUsuarioAndLidaOrderByDataHoraDesc(usuario, false);

                    return notificacoes.stream()
                            .map(this::converterParaDTO)
                            .collect(Collectors.toList());
                });
    }

    // ===== Contar não lidas =====
    public Optional<Long> contarNaoLidas(String emailUsuario) {
        return usuarioRepository.findByEmail(emailUsuario)
                .map(usuario -> notificacaoRepository.countByUsuarioAndLida(usuario, false));
    }

    // ===== Marcar como lida =====
    @Transactional
    public Optional<NotificacaoDTO> marcarComoLida(Long notificacaoId, String emailUsuario) {
        return notificacaoRepository.findById(notificacaoId)
                .map(notificacao -> {
                    if (!notificacao.getUsuario().getEmail().equals(emailUsuario)) {
                        throw new TarefaValidacaoException("Acesso negado");
                    }

                    notificacao.setLida(true);
                    notificacaoRepository.save(notificacao);

                    return converterParaDTO(notificacao);
                });
    }

    // ===== Marcar todas como lidas =====
    @Transactional
    public Optional<Integer> marcarTodasComoLidas(String emailUsuario) {
        return usuarioRepository.findByEmail(emailUsuario)
                .map(usuario -> {
                    List<Notificacao> notificacoes = notificacaoRepository
                            .findByUsuarioAndLidaOrderByDataHoraDesc(usuario, false);

                    for (Notificacao notificacao : notificacoes) {
                        notificacao.setLida(true);
                    }

                    notificacaoRepository.saveAll(notificacoes);

                    return notificacoes.size();
                });
    }

    // ===== Deletar notificação =====
    @Transactional
    public Optional<Void> deletar(Long notificacaoId, String emailUsuario) {
        return notificacaoRepository.findById(notificacaoId)
                .map(notificacao -> {
                    if (!notificacao.getUsuario().getEmail().equals(emailUsuario)) {
                        throw new TarefaValidacaoException("Acesso negado");
                    }

                    notificacaoRepository.delete(notificacao);

                    return (Void) null;
                });
    }

    // ===== Criar e enviar notificação =====
    @Transactional
    public void criarNotificacao(
            TipoNotificacao tipo,
            String titulo,
            String mensagem,
            Usuario destinatario,
            Usuario remetente,
            Tarefa tarefa
    ) {
        Notificacao notificacao = new Notificacao();
        notificacao.setTipo(tipo);
        notificacao.setTitulo(titulo);
        notificacao.setMensagem(mensagem);
        notificacao.setUsuario(destinatario);
        notificacao.setRemetente(remetente);
        notificacao.setTarefa(tarefa);
        notificacao.setLida(false);

        notificacaoRepository.save(notificacao);

        // ✅ Envia via WebSocket
        NotificacaoDTO dto = converterParaDTO(notificacao);
        messagingTemplate.convertAndSend(
                "/topic/notificacoes/" + destinatario.getId(),
                dto
        );
    }

    // ===== Converter para DTO =====
    private NotificacaoDTO converterParaDTO(Notificacao notificacao) {
        NotificacaoDTO dto = new NotificacaoDTO();
        dto.setId(notificacao.getId());
        dto.setTipo(notificacao.getTipo());  // ✅ Isso retorna TipoNotificacao (enum)
        dto.setTitulo(notificacao.getTitulo());
        dto.setMensagem(notificacao.getMensagem());
        dto.setLida(notificacao.getLida());
        dto.setDataHora(notificacao.getDataHora());

        if (notificacao.getTarefa() != null) {
            dto.setTarefaId(notificacao.getTarefa().getId());
            dto.setTarefaTitulo(notificacao.getTarefa().getTitulo());
        }

        if (notificacao.getRemetente() != null) {
            RemetenteDTO remetente = new RemetenteDTO(
                    notificacao.getRemetente().getId(),
                    notificacao.getRemetente().getNome(),
                    notificacao.getRemetente().getFoto()
            );
            dto.setRemetente(remetente);
        }

        return dto;
    }

}
