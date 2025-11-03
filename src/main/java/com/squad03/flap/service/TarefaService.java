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
import com.squad03.flap.util.SegurancaUtils;

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

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MembroService membroService;

    @Autowired
    private MembroRepository membroRepository;

    @Autowired
    private SegurancaUtils segurancaUtils;

    /**
     * Cria uma nova tarefa, buscando as entidades relacionadas e validando os dados.
     */
    public BuscaTarefa criarTarefa(CadastroTarefa cadastroTarefa) {
        try {
            String emailUsuarioLogado = segurancaUtils.getUsuarioLogadoEmail();
            Usuario usuarioCriador = usuarioRepository.findByEmail(emailUsuarioLogado)
                    .orElseThrow(() -> new TarefaValidacaoException("Usuário logado não encontrado no sistema."));

            Optional<Empresa> empresaOptional = empresaRepository.findById(cadastroTarefa.empresaId());
            if (empresaOptional.isEmpty()) {
                throw new TarefaValidacaoException("Empresa não encontrada com ID: " + cadastroTarefa.empresaId());
            }
            Empresa empresa = empresaOptional.get();

            if (cadastroTarefa.listaId() == null) {
                throw new TarefaValidacaoException("O ID da Lista (coluna do Kanban) é obrigatório.");
            }
            Optional<Lista> listaOptional = listaRepository.findById(cadastroTarefa.listaId());
            if (listaOptional.isEmpty()) {
                throw new TarefaValidacaoException("Lista não encontrada com ID: " + cadastroTarefa.listaId());
            }
            Lista lista = listaOptional.get();

            Double proximaPosicao = tarefaRepository.findMaxPosicaoByListaId(lista.getId());
            if (proximaPosicao == null) {
                proximaPosicao = 0.0;
            } else {
                proximaPosicao = proximaPosicao + 1.0;
            }

            StatusTarefa statusDerivado;
            try {
                statusDerivado = StatusTarefa.valueOf(lista.getNome().toUpperCase().replace(" ", "_"));
            } catch (IllegalArgumentException e) {
                statusDerivado = StatusTarefa.A_FAZER;
            }

            Tarefa novaTarefa = Tarefa.builder()
                    .empresa(empresa)
                    .lista(lista)
                    .titulo(cadastroTarefa.titulo())
                    .descricao(cadastroTarefa.descricao())
                    .prioridade(cadastroTarefa.prioridade() != null ? cadastroTarefa.prioridade() : PrioridadeTarefa.MEDIA)
                    .dtEntrega(cadastroTarefa.dtEntrega())
                    .tags(cadastroTarefa.tags() != null ? cadastroTarefa.tags() : new ArrayList<>())
                    .observacoes(cadastroTarefa.observacoes())
                    .status(statusDerivado)
                    .posicao(proximaPosicao)
                    .build();

            Tarefa tarefaSalva = tarefaRepository.save(novaTarefa);

            MembroCreateDTO membroDTO = new MembroCreateDTO();
            membroDTO.setUsuarioId(usuarioCriador.getId());
            membroDTO.setTarefaId(tarefaSalva.getId());
            membroService.criarMembro(membroDTO);

            return converterParaDTO(tarefaSalva);

        } catch (TarefaValidacaoException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar tarefa: " + e.getMessage(), e);
        }
    }

    public List<BuscaTarefa> getAllTarefas() {
        try {
            return tarefaRepository.findAll().stream()
                    .map(this::converterParaDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar todas as tarefas: " + e.getMessage(), e);
        }
    }

    public Optional<BuscaTarefa> getTarefaById(Long id) {
        try {
            return tarefaRepository.findById(id)
                    .map(this::converterParaDTO);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefa por ID: " + e.getMessage(), e);
        }
    }

    public Optional<BuscaTarefa> atualizarTarefa(Long id, AtualizacaoTarefa atualizarDTO) {
        System.out.println("📥 Atualizando tarefa ID: " + id);
        System.out.println("👥 Membros recebidos: " + atualizarDTO.membroIds());

        try {
            return tarefaRepository.findById(id)
                    .map(tarefa -> {
                        // Atualiza campos básicos
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

                        // ✅ ATUALIZAR MEMBROS
                        if (atualizarDTO.membroIds() != null) {
                            System.out.println("🔄 Atualizando membros da tarefa...");

                            // 1. Buscar membros atuais
                            List<Membro> membrosAtuais = membroRepository.findByTarefaId(id);
                            List<Long> usuarioIdsAtuais = membrosAtuais.stream()
                                    .map(m -> m.getUsuario().getId())
                                    .collect(Collectors.toList());

                            // 2. Identificar quem remover
                            List<Membro> membrosParaRemover = membrosAtuais.stream()
                                    .filter(membro -> !atualizarDTO.membroIds().contains(membro.getUsuario().getId()))
                                    .collect(Collectors.toList());

                            // 3. Remover membros não selecionados
                            if (!membrosParaRemover.isEmpty()) {
                                membroRepository.deleteAll(membrosParaRemover);
                                System.out.println("🗑️ Removidos " + membrosParaRemover.size() + " membros");
                            }

                            // 4. Adicionar novos membros
                            for (Long usuarioId : atualizarDTO.membroIds()) {
                                if (!usuarioIdsAtuais.contains(usuarioId)) {
                                    Usuario usuario = usuarioRepository.findById(usuarioId)
                                            .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + usuarioId));

                                    Membro novoMembro = new Membro();
                                    novoMembro.setUsuario(usuario);
                                    novoMembro.setTarefa(tarefa);
                                    membroRepository.save(novoMembro);

                                    System.out.println("✅ Adicionado membro: " + usuario.getNome());
                                }
                            }

                            System.out.println("💾 Membros atualizados com sucesso!");
                        }

                        // Salvar tarefa
                        Tarefa tarefaSalva = tarefaRepository.save(tarefa);
                        return converterParaDTO(tarefaSalva);
                    });
        } catch (Exception e) {
            System.err.println("❌ Erro ao atualizar tarefa: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar tarefa: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Optional<BuscaTarefa> arquivarTarefa(Long id) {
        System.out.println("📦 Arquivando tarefa ID: " + id);
        return tarefaRepository.findById(id)
                .map(tarefa -> {
                    tarefa.setStatus(StatusTarefa.ARQUIVADA);
                    Tarefa tarefaSalva = tarefaRepository.save(tarefa);
                    System.out.println("✅ Tarefa arquivada com sucesso!");
                    return converterParaDTO(tarefaSalva);
                });
    }

    @Transactional
    public Optional<BuscaTarefa> desarquivarTarefa(Long id) {
        System.out.println("🔄 Desarquivando tarefa ID: " + id);
        return tarefaRepository.findById(id)
                .map(tarefa -> {
                    tarefa.setStatus(StatusTarefa.A_FAZER);
                    Tarefa tarefaSalva = tarefaRepository.save(tarefa);
                    System.out.println("✅ Tarefa desarquivada com sucesso!");
                    return converterParaDTO(tarefaSalva);
                });
    }
    @Transactional
    public Optional<BuscaTarefa> moverTarefa(Long id, MoverTarefaDTO moverDTO) {
        String emailUsuarioLogado = segurancaUtils.getUsuarioLogadoEmail();
        Usuario usuarioLogado = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new TarefaValidacaoException("Usuário logado não encontrado no sistema."));

         /*
    boolean isMembro = membroRepository.existsByUsuarioIdAndTarefaId(usuarioLogado.getId(), id);
    System.out.println("🔍 É membro? " + isMembro);

    if (!isMembro) {
        System.err.println("❌ Usuário não é membro da tarefa!");
        throw new TarefaValidacaoException("Acesso negado. Você não é membro desta tarefa para movê-la.");
    }
    */
        return tarefaRepository.findById(id)
                .map(tarefa -> {
                    Lista novaLista = listaRepository.findById(moverDTO.novoListaId())
                            .orElseThrow(() -> new TarefaValidacaoException("Lista de destino não encontrada."));

                    tarefa.setLista(novaLista);

                    int novaPosicaoDesejada = moverDTO.novaPosicao().intValue();

                    List<Tarefa> tarefasDaLista = tarefaRepository.findByListaOrderByPosicaoAsc(novaLista);

                    tarefasDaLista.removeIf(t -> t.getId().equals(id));
                    tarefasDaLista.add(novaPosicaoDesejada, tarefa);

                    for (int i = 0; i < tarefasDaLista.size(); i++) {
                        tarefasDaLista.get(i).setPosicao((double) i);
                    }

                    tarefaRepository.saveAll(tarefasDaLista);

                    return converterParaDTO(tarefa);
                });
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

    public List<BuscaTarefa> getTarefasPorStatus(StatusTarefa status) {
        try {
            return tarefaRepository.findByStatusOrderByPosicaoAsc(status).stream()
                    .map(this::converterParaDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefas por status: " + e.getMessage(), e);
        }
    }

    public List<BuscaTarefa> getTarefasPorPrioridade(PrioridadeTarefa prioridade) {
        try {
            return tarefaRepository.findByPrioridade(prioridade).stream()
                    .map(this::converterParaDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefas por prioridade: " + e.getMessage(), e);
        }
    }

    public List<BuscaTarefa> buscarTarefasPorTitulo(String titulo) {
        try {
            return tarefaRepository.findByTituloContainingIgnoreCase(titulo).stream()
                    .map(this::converterParaDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefas por título: " + e.getMessage(), e);
        }
    }

    public List<BuscaTarefa> buscarTarefasPorDescricao(String descricao) {
        try {
            return tarefaRepository.findByDescricaoContainingIgnoreCase(descricao).stream()
                    .map(this::converterParaDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefas por descrição: " + e.getMessage(), e);
        }
    }

    public List<BuscaTarefa> getTarefasAtrasadas() {
        try {
            return tarefaRepository.findTarefasAtrasadas().stream()
                    .map(this::converterParaDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefas atrasadas: " + e.getMessage(), e);
        }
    }

    public List<BuscaTarefa> getTarefasPorTag(String tag) {
        try {
            return tarefaRepository.findByTag(tag).stream()
                    .map(this::converterParaDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefas por tag: " + e.getMessage(), e);
        }
    }

    public List<BuscaTarefa> getTarefasPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        try {
            return tarefaRepository.findByDtEntregaBetween(dataInicio, dataFim).stream()
                    .map(this::converterParaDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefas por período: " + e.getMessage(), e);
        }
    }

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

    public List<BuscaTarefa> getTarefasPorEmpresa(Long empresaId) {
        if (!empresaRepository.existsById(empresaId)) {
            throw new TarefaValidacaoException("Empresa não encontrada com ID: " + empresaId);
        }

        return tarefaRepository.findByEmpresaId(empresaId).stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public List<BuscaTarefa> getTarefasPorLista(Long listaId) {
        try {
            if (!listaRepository.existsById(listaId)) {
                throw new TarefaValidacaoException("Lista não encontrada com ID: " + listaId);
            }

            return tarefaRepository.findByListaId(listaId).stream()
                    .map(this::converterParaDTO)
                    .toList();

        } catch (TarefaValidacaoException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefas por lista: " + e.getMessage(), e);
        }
    }

    // ✅ MÉTODO ATUALIZADO - CONVERTE TAREFA PARA BUSCATAREFA
    // ✅ MÉTODO ATUALIZADO - CONVERTE TAREFA PARA BUSCATAREFA
    private BuscaTarefa converterParaDTO(Tarefa tarefa) {
        try {
            // ✅ Buscar membros da tarefa
            List<Membro> membros = membroRepository.findByTarefaId(tarefa.getId());

            // ✅ Extrair IDs dos membros (tabela membro)
            List<Long> membroIds = membros.stream()
                    .map(Membro::getId)
                    .collect(Collectors.toList());

            // ✅ Extrair IDs dos usuários (para filtro)
            List<Long> usuarioIds = membros.stream()
                    .map(membro -> membro.getUsuario().getId())
                    .collect(Collectors.toList());

            // ✅ Criar lista de MembroSimplificadoDTO com username
            List<MembroSimplificadoDTO> membrosDTO = membros.stream()
                    .map(membro -> new MembroSimplificadoDTO(
                            membro.getId(),
                            membro.getUsuario().getId(),
                            membro.getUsuario().getNome(),
                            membro.getUsuario().getEmail(),  // ✅ username = email
                            membro.getUsuario().getFoto()
                    ))
                    .collect(Collectors.toList());

            return new BuscaTarefa(
                    tarefa.getId(),
                    tarefa.getEmpresa() != null ? tarefa.getEmpresa().getId() : null,
                    tarefa.getEmpresa() != null ? tarefa.getEmpresa().getNome() : null,
                    tarefa.getLista() != null ? tarefa.getLista().getId() : null,
                    tarefa.getTitulo(),
                    tarefa.getDescricao(),
                    tarefa.getStatus(),
                    tarefa.getPrioridade(),
                    tarefa.getPosicao(),
                    tarefa.getDtCriacao(),
                    tarefa.getDtEntrega(),
                    tarefa.getDtConclusao(),
                    tarefa.getTags() != null ? tarefa.getTags() : new ArrayList<>(),
                    tarefa.getObservacoes(),
                    membroIds,     // ✅ IDs DOS MEMBROS
                    usuarioIds,    // ✅ IDs DOS USUÁRIOS (PARA FILTRO)
                    membrosDTO     // ✅ DETALHES DOS MEMBROS COM USERNAME
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter tarefa para DTO: " + e.getMessage(), e);
        }
    }


    // ✅ MÉTODO ATUALIZADO - DETALHAR TAREFA
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

                    // ✅ Buscar membros com username
                    List<Membro> membros = membroRepository.findByTarefaId(tarefa.getId());
                    List<MembroSimplificadoDTO> membrosDTO = membros.stream()
                            .map(membro -> new MembroSimplificadoDTO(
                                    membro.getId(),
                                    membro.getUsuario().getId(),
                                    membro.getUsuario().getNome(),
                                    membro.getUsuario().getEmail(),  // ✅ username = email
                                    membro.getUsuario().getFoto()
                            ))
                            .collect(Collectors.toList());

                    return new DetalheTarefa(
                            tarefa.getId(),
                            tarefa.getEmpresa() != null ? tarefa.getEmpresa().getId() : null,
                            tarefa.getEmpresa() != null ? tarefa.getEmpresa().getNome() : null,
                            tarefa.getLista() != null ? tarefa.getLista().getId() : null,
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
                            membrosDTO,  // ✅ MEMBROS COM USERNAME
                            null,        // historico
                            tarefa.getObservacoes()
                    );
                });
    }

}
