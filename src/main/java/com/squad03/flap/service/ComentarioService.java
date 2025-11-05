package com.squad03.flap.service;

import com.squad03.flap.DTO.ComentarioRequestDTO;
import com.squad03.flap.DTO.ComentarioResponseDTO;
import com.squad03.flap.model.Comentario;
import com.squad03.flap.model.Tarefa;
import com.squad03.flap.model.Usuario;
import com.squad03.flap.repository.ComentarioRepository;
import com.squad03.flap.repository.TarefaRepository;
import com.squad03.flap.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Transactional
    public ComentarioResponseDTO criarComentario(ComentarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + dto.getUsuarioId()));

        Tarefa tarefa = tarefaRepository.findById(dto.getTarefaId())
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada com ID: " + dto.getTarefaId()));

        Comentario comentario = new Comentario();
        comentario.setTexto(dto.getTexto());
        comentario.setUsuario(usuario);
        comentario.setTarefa(tarefa);

        Comentario salvo = comentarioRepository.save(comentario);
        return converterParaDTO(salvo);
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
        return new ComentarioResponseDTO(comentario);  // ✅ Usa o novo construtor
    }

}