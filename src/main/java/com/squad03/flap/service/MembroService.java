package com.squad03.flap.service;

import com.squad03.flap.DTO.MembroCreateDTO;
import com.squad03.flap.DTO.MembroDTO;
import com.squad03.flap.model.Membro;
import com.squad03.flap.model.Tarefa;
import com.squad03.flap.model.Usuario;
import com.squad03.flap.repository.MembroRepository;
import com.squad03.flap.repository.TarefaRepository;
import com.squad03.flap.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MembroService {

    @Autowired
    private MembroRepository membroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Transactional
    public MembroDTO criarMembro(MembroCreateDTO createDTO) {
        Usuario usuario = usuarioRepository.findById(createDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + createDTO.getUsuarioId()));

        Tarefa tarefa = tarefaRepository.findById(createDTO.getTarefaId())
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada com ID: " + createDTO.getTarefaId()));

        if (membroRepository.existsByUsuarioIdAndTarefaId(createDTO.getUsuarioId(), createDTO.getTarefaId())) {
            throw new RuntimeException("Já existe um membro com este usuário e tarefa");
        }

        Membro membro = new Membro(usuario, tarefa);
        Membro membroSalvo = membroRepository.save(membro);

        return MembroDTO.fromEntity(membroSalvo);
    }

    @Transactional(readOnly = true)
    public List<MembroDTO> buscarTodosMembros() {
        return membroRepository.findAll().stream()
                .map(MembroDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MembroDTO buscarMembroPorId(Long id) {
        Membro membro = membroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membro não encontrado com ID: " + id));
        return MembroDTO.fromEntity(membro);
    }

    @Transactional(readOnly = true)
    public List<MembroDTO> buscarMembrosPorUsuario(Long usuarioId) {
        return membroRepository.findByUsuarioId(usuarioId).stream()
                .map(MembroDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MembroDTO> buscarMembrosPorTarefa(Long tarefaId) {
        return membroRepository.findByTarefaId(tarefaId).stream()
                .map(MembroDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public MembroDTO atualizarMembro(Long id, MembroCreateDTO updateDTO) {
        Membro membro = membroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membro não encontrado com ID: " + id));

        Usuario usuario = usuarioRepository.findById(updateDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + updateDTO.getUsuarioId()));

        Tarefa tarefa = tarefaRepository.findById(updateDTO.getTarefaId())
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada com ID: " + updateDTO.getTarefaId()));

        membroRepository.findByUsuarioIdAndTarefaId(updateDTO.getUsuarioId(), updateDTO.getTarefaId())
                .ifPresent(membroExistente -> {
                    if (!membroExistente.getId().equals(id)) {
                        throw new RuntimeException("Já existe outro membro com este usuário e tarefa");
                    }
                });

        membro.setUsuario(usuario);
        membro.setTarefa(tarefa);

        Membro membroAtualizado = membroRepository.save(membro);
        return MembroDTO.fromEntity(membroAtualizado);
    }

    @Transactional
    public void deletarMembro(Long id) {
        if (!membroRepository.existsById(id)) {
            throw new RuntimeException("Membro não encontrado com ID: " + id);
        }
        membroRepository.deleteById(id);
    }

    @Transactional
    public void deletarMembrosPorTarefa(Long tarefaId) {
        membroRepository.deleteByTarefaId(tarefaId);
    }

    @Transactional
    public void deletarMembrosPorUsuario(Long usuarioId) {
        membroRepository.deleteByUsuarioId(usuarioId);
    }
}
