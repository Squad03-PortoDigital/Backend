package com.squad03.flap.service;

import com.squad03.flap.model.Empresa;
import com.squad03.flap.model.Lista;
import com.squad03.flap.repository.AgenteRepository;
import com.squad03.flap.repository.EmpresaRepository;
import com.squad03.flap.repository.ListaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.squad03.flap.DTO.*;
import com.squad03.flap.model.Agente;
import com.squad03.flap.model.Tarefa;
import com.squad03.flap.model.Tarefa.StatusTarefa;
import com.squad03.flap.model.Tarefa.PrioridadeTarefa;
import com.squad03.flap.repository.TarefaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
@Transactional
public class TarefaService {
    
    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private AgenteRepository agenteRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ListaRepository listaRepository;

    public TarefaDTO criarTarefa(CriarTarefaDTO criarTarefaDTO,int agenteId, Long empresaId, Long listaId) {

        Agente agente = agenteRepository.findById(agenteId).get();

        Empresa empresa = empresaRepository.findById(empresaId).get();

        Lista lista = listaRepository.findById(listaId).get();

        try {
            Integer proximaPosicao = tarefaRepository.findMaxPosicaoByAgenteAndStatus(agente, StatusTarefa.A_FAZER);
            if (proximaPosicao == null) {
                proximaPosicao = 0;
            } else {
                proximaPosicao++;
            }

            Tarefa novaTarefa = Tarefa.builder()
                    .agente(agente)
                    .titulo(criarTarefaDTO.titulo())
                    .descricao(criarTarefaDTO.descricao())
                    .prioridade(criarTarefaDTO.prioridade() != null ? criarTarefaDTO.prioridade() : PrioridadeTarefa.MEDIA)
                    .dtEntrega(criarTarefaDTO.dtEntrega())
                    .tags(criarTarefaDTO.tags() != null ? criarTarefaDTO.tags() : new ArrayList<>())
                    .observacoes(criarTarefaDTO.observacoes())
                    .status(StatusTarefa.A_FAZER)
                    .posicao(proximaPosicao)
                    .empresas(empresa)
                    .listas(lista)
                    .build();

            Tarefa tarefaSalva = tarefaRepository.save(novaTarefa);
            return converterParaDTO(tarefaSalva);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar tarefa: " + e.getMessage(), e);
        }
    }
    
    public List<TarefaDTO> getAllTarefas() {
        try {
            return tarefaRepository.findAll().stream()
                    .map(this::converterParaDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar todas as tarefas: " + e.getMessage(), e);
        }
    }

    public Optional<TarefaDTO> getTarefaById(Long id) {
        try {
            return tarefaRepository.findById(id)
                    .map(this::converterParaDTO);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefa por ID: " + e.getMessage(), e);
        }
    }

    public List<TarefaDTO> getTarefasPorAgente(Agente agente) {
        try {
            return tarefaRepository.findByAgenteOrderByPosicaoAsc(agente).stream()
                    .map(this::converterParaDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefas por agente: " + e.getMessage(), e);
        }
    }

    public List<TarefaDTO> getTarefasPorStatus(StatusTarefa status) {
        try {
            return tarefaRepository.findByStatusOrderByPosicaoAsc(status).stream()
                    .map(this::converterParaDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefas por status: " + e.getMessage(), e);
        }
    }

    public List<TarefaDTO> getTarefasPorPrioridade(PrioridadeTarefa prioridade) {
        try {
            return tarefaRepository.findByPrioridade(prioridade).stream()
                    .map(this::converterParaDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefas por prioridade: " + e.getMessage(), e);
        }
    }

    public Optional<TarefaDTO> atualizarTarefa(Long id, AtualizarTarefaDTO atualizarDTO) {
        try {
            return tarefaRepository.findById(id)
                    .map(tarefa -> {
                        if (atualizarDTO.titulo() != null) {
                            tarefa.setTitulo(atualizarDTO.titulo());
                        }
                        if (atualizarDTO.descricao() != null) {
                            tarefa.setDescricao(atualizarDTO.descricao());
                        }
                        if (atualizarDTO.status() != null) {
                            tarefa.setStatus(atualizarDTO.status());
                        }
                        if (atualizarDTO.prioridade() != null) {
                            tarefa.setPrioridade(atualizarDTO.prioridade());
                        }
                        if (atualizarDTO.dtEntrega() != null) {
                            tarefa.setDtEntrega(atualizarDTO.dtEntrega());
                        }
                        if (atualizarDTO.tags() != null) {
                            tarefa.setTags(atualizarDTO.tags());
                        }
                        if (atualizarDTO.observacoes() != null) {
                            tarefa.setObservacoes(atualizarDTO.observacoes());
                        }
                        
                        return converterParaDTO(tarefaRepository.save(tarefa));
                    });
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar tarefa: " + e.getMessage(), e);
        }
    }

    public Optional<TarefaDTO> moverTarefa(Long id, MoverTarefaDTO moverDTO) {
        try {
            return tarefaRepository.findById(id)
                    .map(tarefa -> {
                        // Atualizar status se fornecido
                        if (moverDTO.novoStatus() != null) {
                            tarefa.setStatus(moverDTO.novoStatus());
                        }
                        
                        // Atualizar posição se fornecida
                        if (moverDTO.novaPosicao() != null) {
                            tarefa.setPosicao(moverDTO.novaPosicao());
                        }
                        
                        return converterParaDTO(tarefaRepository.save(tarefa));
                    });
        } catch (Exception e) {
            throw new RuntimeException("Erro ao mover tarefa: " + e.getMessage(), e);
        }
    }

    public boolean deletarTarefa(Long id) {
        try {
            if (tarefaRepository.existsById(id)) {
                tarefaRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar tarefa: " + e.getMessage(), e);
        }
    }

    public List<TarefaDTO> buscarTarefasPorTitulo(String titulo) {
        try {
            return tarefaRepository.findByTituloContainingIgnoreCase(titulo).stream()
                    .map(this::converterParaDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefas por título: " + e.getMessage(), e);
        }
    }

    public List<TarefaDTO> buscarTarefasPorDescricao(String descricao) {
        try {
            return tarefaRepository.findByDescricaoContainingIgnoreCase(descricao).stream()
                    .map(this::converterParaDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefas por descrição: " + e.getMessage(), e);
        }
    }

    public List<TarefaDTO> getTarefasAtrasadas() {
        try {
            return tarefaRepository.findTarefasAtrasadas().stream()
                    .map(this::converterParaDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefas atrasadas: " + e.getMessage(), e);
        }
    }

    public List<TarefaDTO> getTarefasPorTag(String tag) {
        try {
            return tarefaRepository.findByTag(tag).stream()
                    .map(this::converterParaDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefas por tag: " + e.getMessage(), e);
        }
    }

    public List<TarefaDTO> getTarefasPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        try {
            return tarefaRepository.findByDtEntregaBetween(dataInicio, dataFim).stream()
                    .map(this::converterParaDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefas por período: " + e.getMessage(), e);
        }
    }

    public Long contarTarefasPorAgenteEStatus(Agente agente, StatusTarefa status) {
        try {
            return tarefaRepository.countByAgenteAndStatus(agente, status);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao contar tarefas: " + e.getMessage(), e);
        }
    }

    private TarefaDTO converterParaDTO(Tarefa tarefa) {
        try {
            return new TarefaDTO(
                tarefa.getId(),
                tarefa.getAgente() != null ? Long.valueOf(tarefa.getAgente().getId()) : null,
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getStatus(),
                tarefa.getPrioridade(),
                tarefa.getPosicao(),
                tarefa.getDtCriacao(),
                tarefa.getDtEntrega(),
                tarefa.getDtConclusao(),
                tarefa.getTags() != null ? tarefa.getTags() : new ArrayList<>(),
                tarefa.getObservacoes()
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter tarefa para DTO: " + e.getMessage(), e);
        }
    }
}
