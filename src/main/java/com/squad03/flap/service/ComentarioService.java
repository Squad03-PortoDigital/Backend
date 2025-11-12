package com.squad03.flap.service;

import com.squad03.flap.DTO.ComentarioRequestDTO;
import com.squad03.flap.DTO.ComentarioResponseDTO;
import com.squad03.flap.model.*;
import com.squad03.flap.repository.ComentarioRepository;
import com.squad03.flap.repository.MembroRepository;
import com.squad03.flap.repository.TarefaRepository;
import com.squad03.flap.repository.UsuarioRepository;
import com.squad03.flap.util.SegurancaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private MembroRepository membroRepository;

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private SegurancaUtils segurancaUtils;

    @Transactional
    public ComentarioResponseDTO criarComentario(ComentarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + dto.getUsuarioId()));

        Tarefa tarefa = tarefaRepository.findById(dto.getTarefaId())
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada com ID: " + dto.getTarefaId()));

        // ✅ Cria comentário
        Comentario comentario = new Comentario();
        comentario.setTexto(dto.getTexto());
        comentario.setUsuario(usuario);
        comentario.setTarefa(tarefa);

        Comentario salvo = comentarioRepository.save(comentario);

        // ✅ ✅ ✅ NOVO: Notifica membros da tarefa ✅ ✅ ✅
        try {
            List<Membro> membros = membroRepository.findByTarefaId(dto.getTarefaId());

            for (Membro membro : membros) {
                // Não notifica quem comentou
                if (!membro.getUsuario().getId().equals(usuario.getId())) {
                    notificacaoService.criarNotificacao(
                            TipoNotificacao.COMENTARIO,
                            "Novo comentário",
                            usuario.getNome() + " comentou na tarefa '" + tarefa.getTitulo() + "'",
                            membro.getUsuario(),  // Destinatário
                            usuario,              // Quem comentou
                            tarefa
                    );
                }
            }

            // ✅ Verifica menções (@usuario)
            verificarMencoes(dto.getTexto(), tarefa, usuario);

        } catch (Exception e) {
            System.err.println("⚠️ Erro ao enviar notificações: " + e.getMessage());
            // Não falha a criação do comentário se notificação der erro
        }
        // ✅ ✅ ✅ FIM DAS NOTIFICAÇÕES ✅ ✅ ✅

        return converterParaDTO(salvo);
    }

    private void verificarMencoes(String texto, Tarefa tarefa, Usuario remetente) {
        // Regex para detectar @nome
        Pattern pattern = Pattern.compile("@([\\w.]+)");
        Matcher matcher = pattern.matcher(texto);

        while (matcher.find()) {
            String mencao = matcher.group(1).toLowerCase();

            // Busca todos os usuários
            List<Usuario> todosUsuarios = usuarioRepository.findAll();

            for (Usuario usuario : todosUsuarios) {
                // Verifica se o nome contém a menção (case-insensitive)
                String nomeCompletoSemEspacos = usuario.getNome().toLowerCase().replace(" ", ".");
                String nomeSimples = usuario.getNome().toLowerCase().split(" ")[0]; // Primeiro nome

                // Match: @joao, @joao.silva, @João, etc
                if (nomeSimples.equals(mencao) ||
                        nomeCompletoSemEspacos.equals(mencao) ||
                        usuario.getNome().toLowerCase().contains(mencao)) {

                    // Não notifica se mencionou a si mesmo
                    if (!usuario.getId().equals(remetente.getId())) {
                        try {
                            String preview = texto.length() > 50 ?
                                    texto.substring(0, 50) + "..." : texto;

                            notificacaoService.criarNotificacao(
                                    TipoNotificacao.MENCAO,
                                    "Você foi mencionado",
                                    remetente.getNome() + " mencionou você: \"" + preview + "\"",
                                    usuario,      // Destinatário
                                    remetente,    // Quem mencionou
                                    tarefa
                            );

                            break; // Para de buscar depois de encontrar
                        } catch (Exception e) {
                            System.err.println("⚠️ Erro ao enviar notificação de menção: " + e.getMessage());
                        }
                    }
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public List<ComentarioResponseDTO> listarTodos() {
        return comentarioRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ComentarioResponseDTO buscarPorId(Long id) {
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentário não encontrado com ID: " + id));
        return converterParaDTO(comentario);
    }

    @Transactional(readOnly = true)
    public List<ComentarioResponseDTO> buscarPorTarefa(Long tarefaId) {
        return comentarioRepository.findByTarefaId(tarefaId).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ComentarioResponseDTO> buscarPorUsuario(Long usuarioId) {
        return comentarioRepository.findByUsuarioId(usuarioId).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ComentarioResponseDTO> buscarPorUsuarioETarefa(Long usuarioId, Long tarefaId) {
        return comentarioRepository.findByUsuarioIdAndTarefaId(usuarioId, tarefaId).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ComentarioResponseDTO atualizarComentario(Long id, ComentarioRequestDTO dto) {
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentário não encontrado com ID: " + id));

        if (!comentario.getUsuario().getId().equals(dto.getUsuarioId())) {
            throw new RuntimeException("Usuário não autorizado a atualizar este comentário");
        }

        comentario.setTexto(dto.getTexto());
        Comentario atualizado = comentarioRepository.save(comentario);
        return converterParaDTO(atualizado);
    }

    @Transactional
    public void deletarComentario(Long id, Long usuarioId) {
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentário não encontrado com ID: " + id));

        if (!comentario.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("Usuário não autorizado a deletar este comentário");
        }

        comentarioRepository.deleteById(id);
    }

    private ComentarioResponseDTO converterParaDTO(Comentario comentario) {
        return new ComentarioResponseDTO(comentario);
    }
}
