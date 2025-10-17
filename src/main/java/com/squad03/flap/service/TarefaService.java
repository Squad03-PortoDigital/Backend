package com.squad03.flap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.squad03.flap.DTO.*;
import com.squad03.flap.exception.TarefaValidacaoException;
import com.squad03.flap.model.*;
import com.squad03.flap.model.Tarefa.StatusTarefa;
import com.squad03.flap.model.Tarefa.PrioridadeTarefa;
import com.squad03.flap.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@Transactional
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private AnexoRepository anexoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ListaRepository listaRepository;

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private ComentarioService comentarioService;

    /**
     * Cria uma nova tarefa, buscando as entidades relacionadas e validando os dados.
     * @param cadastroTarefa O DTO com os dados da tarefa a ser criada.
     * @return O DTO da tarefa salva.
     */
    public BuscaTarefa criarTarefa(CadastroTarefa cadastroTarefa) {
        try {
            // Busca e valida as entidades relacionadas
            Optional<Empresa> empresaOptional = empresaRepository.findById(cadastroTarefa.empresaId());
            if (empresaOptional.isEmpty()) {
                throw new TarefaValidacaoException("Empresa não encontrada com ID: " + cadastroTarefa.empresaId());
            }
            Empresa empresa = empresaOptional.get();

            Optional<Lista> listaOptional = listaRepository.findById(cadastroTarefa.listaId());
            if (listaOptional.isEmpty()) {
                throw new TarefaValidacaoException("Lista não encontrada com ID: " + cadastroTarefa.listaId());
            }
            Lista lista = listaOptional.get();

           //Integer proximaPosicao = tarefaRepository.findMaxPosicaoByAgenteAndStatus(agente, StatusTarefa.A_FAZER);
            //if (proximaPosicao == null) {
            //    proximaPosicao = 0;
            //} else {
            //    proximaPosicao++;
            //}

            Tarefa novaTarefa = Tarefa.builder()
                    .empresa(empresa)
                    .lista(lista)
                    .titulo(cadastroTarefa.titulo())
                    .descricao(cadastroTarefa.descricao())
                    .prioridade(cadastroTarefa.prioridade() != null ? cadastroTarefa.prioridade() : PrioridadeTarefa.MEDIA)
                    .dtEntrega(cadastroTarefa.dtEntrega())
                    .tags(cadastroTarefa.tags() != null ? cadastroTarefa.tags() : new ArrayList<>())
                    .observacoes(cadastroTarefa.observacoes())
                    .status(StatusTarefa.A_FAZER)
                    //.posicao(proximaPosicao)
                    .build();

            Tarefa tarefaSalva = tarefaRepository.save(novaTarefa);
            return converterParaDTO(tarefaSalva);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar tarefa: " + e.getMessage(), e);
        }
    }

    /**
     * Busca todas as tarefas.
     * @return Uma lista de DTOs de busca de tarefa.
     */
    public List<BuscaTarefa> getAllTarefas() {
        try {
            return tarefaRepository.findAll().stream()
                    .map(this::converterParaDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar todas as tarefas: " + e.getMessage(), e);
        }
    }

    /**
     * Busca uma tarefa por ID.
     * @param id O ID da tarefa.
     * @return Um DTO de busca de tarefa, se encontrado.
     */
    public Optional<BuscaTarefa> getTarefaById(Long id) {
        try {
            return tarefaRepository.findById(id)
                    .map(this::converterParaDTO);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefa por ID: " + e.getMessage(), e);
        }
    }

    /**
     * Atualiza uma tarefa existente no banco de dados.
     * @param id O ID da tarefa a ser atualizada.
     * @param atualizarDTO Um DTO com os novos dados.
     * @return O DTO com os dados da tarefa atualizada.
     */
    public Optional<BuscaTarefa> atualizarTarefa(Long id, AtualizacaoTarefa atualizarDTO) {
        try {
            return tarefaRepository.findById(id)
                    .map(tarefa -> {
                        // Lógica de atualização dos campos
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
                        // Salvando a tarefa e retornando o DTO
                        return converterParaDTO(tarefaRepository.save(tarefa));
                    });
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar tarefa: " + e.getMessage(), e);
        }
    }

    /**
     * Move uma tarefa, atualizando sua posição e/ou status.
     * @param id O ID da tarefa.
     * @param moverDTO O DTO com a nova posição e status.
     * @return O DTO da tarefa movida.
     */
    public Optional<BuscaTarefa> moverTarefa(Long id, MoverTarefaDTO moverDTO) {
        try {
            return tarefaRepository.findById(id)
                    .map(tarefa -> {
                        if (moverDTO.novoStatus() != null) {
                            tarefa.setStatus(moverDTO.novoStatus());
                        }
                        if (moverDTO.novaPosicao() != null) {
                            tarefa.setPosicao(moverDTO.novaPosicao());
                        }
                        return converterParaDTO(tarefaRepository.save(tarefa));
                    });
        } catch (Exception e) {
            throw new RuntimeException("Erro ao mover tarefa: " + e.getMessage(), e);
        }
    }

    /**
     * Deleta uma tarefa por ID.
     * @param id O ID da tarefa a ser deletada.
     * @return true se a tarefa foi deletada, false caso contrário.
     */
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

    /**
     * Busca tarefas por agente.
     * @param agente A entidade Agente.
     * @return Uma lista de DTOs de tarefas.
     */


    /**
     * Busca tarefas por status.
     * @param status O status da tarefa.
     * @return Uma lista de DTOs de tarefas.
     */
    public List<BuscaTarefa> getTarefasPorStatus(StatusTarefa status) {
        try {
            return tarefaRepository.findByStatusOrderByPosicaoAsc(status).stream()
                    .map(this::converterParaDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefas por status: " + e.getMessage(), e);
        }
    }

    /**
     * Busca tarefas por prioridade.
     * @param prioridade A prioridade da tarefa.
     * @return Uma lista de DTOs de tarefas.
     */
    public List<BuscaTarefa> getTarefasPorPrioridade(PrioridadeTarefa prioridade) {
        try {
            return tarefaRepository.findByPrioridade(prioridade).stream()
                    .map(this::converterParaDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefas por prioridade: " + e.getMessage(), e);
        }
    }

    /**
     * Busca tarefas por título.
     * @param titulo O título para buscar.
     * @return Uma lista de DTOs de tarefas.
     */
    public List<BuscaTarefa> buscarTarefasPorTitulo(String titulo) {
        try {
            return tarefaRepository.findByTituloContainingIgnoreCase(titulo).stream()
                    .map(this::converterParaDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefas por título: " + e.getMessage(), e);
        }
    }

    /**
     * Busca tarefas por descrição.
     * @param descricao A descrição para buscar.
     * @return Uma lista de DTOs de tarefas.
     */
    public List<BuscaTarefa> buscarTarefasPorDescricao(String descricao) {
        try {
            return tarefaRepository.findByDescricaoContainingIgnoreCase(descricao).stream()
                    .map(this::converterParaDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefas por descrição: " + e.getMessage(), e);
        }
    }

    /**
     * Busca tarefas atrasadas.
     * @return Uma lista de DTOs de tarefas atrasadas.
     */
    public List<BuscaTarefa> getTarefasAtrasadas() {
        try {
            return tarefaRepository.findTarefasAtrasadas().stream()
                    .map(this::converterParaDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefas atrasadas: " + e.getMessage(), e);
        }
    }

    /**
     * Busca tarefas por tag.
     * @param tag A tag para filtrar.
     * @return Uma lista de DTOs de tarefas.
     */
    public List<BuscaTarefa> getTarefasPorTag(String tag) {
        try {
            return tarefaRepository.findByTag(tag).stream()
                    .map(this::converterParaDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefas por tag: " + e.getMessage(), e);
        }
    }

    /**
     * Busca tarefas por período de entrega.
     * @param dataInicio A data de início do período.
     * @param dataFim A data de fim do período.
     * @return Uma lista de DTOs de tarefas.
     */
    public List<BuscaTarefa> getTarefasPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        try {
            return tarefaRepository.findByDtEntregaBetween(dataInicio, dataFim).stream()
                    .map(this::converterParaDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefas por período: " + e.getMessage(), e);
        }
    }

    /**
     * Busca os anexos de uma tarefa específica.
     * @param tarefaId O ID da tarefa.
     * @return Uma lista de DTOs de Anexo.
     */
    public List<BuscaAnexo> getAnexosPorTarefa(Long tarefaId) {
        try {
            return anexoRepository.findByTarefaId(tarefaId).stream()
                    .map(anexo -> new BuscaAnexo(
                            anexo.getId(),
                            anexo.getLink(),
                            anexo.getDescricao(),
                            anexo.getTarefa().getId()
                    ))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar anexos da tarefa: " + e.getMessage(), e);
        }
    }

    public List<BuscaTarefa> getTarefasPorEmpresa(Long empresaId){
        if (!empresaRepository.existsById(empresaId)) {
            throw new TarefaValidacaoException("Empresa não encontrada com ID: " + empresaId);
        }

        return tarefaRepository.findByEmpresaId(empresaId).stream()
                .map(this::converterParaDTO)
                .toList();
    }

    private BuscaTarefa converterParaDTO(Tarefa tarefa) {
        try {
            return new BuscaTarefa(
                    tarefa.getId(),
                    tarefa.getEmpresa() != null ? tarefa.getEmpresa().getId() : null,
                    tarefa.getLista() != null ? tarefa.getLista().getId() : null, // <-- Adicionado ao retorno
                    tarefa.getTitulo(),
                    tarefa.getDescricao(),
                    tarefa.getStatus(),
                    tarefa.getPrioridade(),
                    tarefa.getPosicao(),
                    tarefa.getDtCriacao(),
                    tarefa.getDtEntrega(),
                    tarefa.getDtConclusao(),
                    tarefa.getTags() != null ? tarefa.getTags() : new ArrayList<String>(),
                    tarefa.getObservacoes()
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter tarefa para DTO: " + e.getMessage(), e);
        }
    }

    public Optional<DetalheTarefa> detalharTarefa(Long id) {
        return tarefaRepository.findById(id)
                .map(tarefa -> {
                    List<BuscaAnexo> anexosDTO = anexoRepository.findByTarefaId(tarefa.getId()).stream()
                            .map(anexo -> new BuscaAnexo(
                                    anexo.getId(),
                                    anexo.getLink(),
                                    anexo.getDescricao(),
                                    anexo.getTarefa().getId()
                            )).toList();

                    List<BuscaChecklist> checklistsDTO = checklistRepository.findByTarefaId(tarefa.getId()).stream()
                            .map(BuscaChecklist::new)
                            .toList();

                    List<ComentarioResponseDTO> comentariosDTO = comentarioService.buscarPorTarefa(tarefa.getId());

                    return new DetalheTarefa(
                            tarefa.getId(),
                            tarefa.getEmpresa() != null ? tarefa.getEmpresa().getId() : null,
                            tarefa.getTitulo(),
                            tarefa.getDescricao(),
                            tarefa.getStatus(),
                            tarefa.getPrioridade(),
                            tarefa.getPosicao(),
                            tarefa.getDtCriacao(),
                            tarefa.getDtEntrega(),
                            tarefa.getDtConclusao(),
                            tarefa.getTags(),
                            anexosDTO,
                            checklistsDTO,
                            comentariosDTO,
                            null,
                            tarefa.getObservacoes()
                    );
                });
    }
}