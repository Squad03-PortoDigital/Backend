package com.squad03.flap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.squad03.flap.DTO.*;
import com.squad03.flap.exception.TarefaValidacaoException;
import com.squad03.flap.model.*;
import com.squad03.flap.model.Tarefa.StatusTarefa;
import com.squad03.flap.model.Tarefa.PrioridadeTarefa;
import com.squad03.flap.repository.*;
import com.squad03.flap.util.SegurancaUtils;
import com.squad03.flap.service.NotificacaoService;

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
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private AnexoRepository anexoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private DropboxService dropboxService;

    @Autowired
    private ListaRepository listaRepository;

    @Autowired
    private GoogleCalendarService googleCalendarService;

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MembroService membroService;

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private MembroRepository membroRepository;

    @Autowired
    private SegurancaUtils segurancaUtils;

    private String sanitizeName(String name) {
        if (name == null || name.isEmpty()) {
            return "Sem-Nome";
        }

        return name
                .trim()
                .replaceAll("[^a-zA-Z0-9\\sÀ-ÿ-]", "") // Remove caracteres especiais, mantém acentos
                .replaceAll("\\s+", "-") // Substitui espaços por hífens
                .replaceAll("-+", "-") // Remove hífens duplicados
                .substring(0, Math.min(name.length(), 50)); // Limita a 50 caracteres
    }

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

            // ✅ CRIAR PASTA NO DROPBOX
            try {
                String empresaPasta = sanitizeName(empresa.getNome());
                String tarefaPasta = "Tarefa-" + tarefaSalva.getId() + "-" + sanitizeName(tarefaSalva.getTitulo());
                String fullPath = "/" + empresaPasta + "/" + tarefaPasta;

                dropboxService.createFolder(fullPath);

                tarefaSalva.setDropboxPath(fullPath);
                tarefaRepository.save(tarefaSalva);

                System.out.println("📁 Pasta criada no Dropbox: " + fullPath);
            } catch (Exception e) {
                System.err.println("⚠️ Erro ao criar pasta no Dropbox: " + e.getMessage());
                e.printStackTrace();
            }

            MembroCreateDTO membroDTO = new MembroCreateDTO();
            membroDTO.setUsuarioId(usuarioCriador.getId());
            membroDTO.setTarefaId(tarefaSalva.getId());
            membroService.criarMembro(membroDTO);

            BuscaTarefa tarefaDTO = converterParaDTO(tarefaSalva);

            TarefaEventoDTO evento = new TarefaEventoDTO(
                    "CRIADA",
                    tarefaDTO.id(),
                    tarefaDTO.listaId(),
                    null,
                    tarefaDTO.posicao().intValue(),
                    tarefaDTO,
                    usuarioCriador.getNome()
            );
            messagingTemplate.convertAndSend("/topic/tarefas", evento);

            return tarefaDTO;

        } catch (TarefaValidacaoException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar tarefa: " + e.getMessage(), e);
        }
    }



    // ✅ OTIMIZADO - USA findAllWithDetails()
    public List<BuscaTarefa> getAllTarefas() {
        try {
            return tarefaRepository.findAllWithDetails().stream()
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
                        // ✅ GUARDA O TÍTULO ANTIGO E PATH ANTIGO PARA COMPARAÇÃO
                        String tituloAntigo = tarefa.getTitulo();
                        String dropboxPathAntigo = tarefa.getDropboxPath();

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
                        LocalDateTime dataEntregaAntiga = tarefa.getDtEntrega();
                        boolean dataDeEntregaFoiAdicionada = (dataEntregaAntiga == null && atualizarDTO.dtEntrega() != null);

                        if (atualizarDTO.dtEntrega() != null) {
                            tarefa.setDtEntrega(atualizarDTO.dtEntrega());
                        }
                        if (atualizarDTO.concluida() != null) {
                            tarefa.setConcluida(atualizarDTO.concluida());
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

                                    try {
                                        Usuario remetente = usuarioRepository.findByEmail(segurancaUtils.getUsuarioLogadoEmail())
                                                .orElse(null);

                                        notificacaoService.criarNotificacao(
                                                TipoNotificacao.ATRIBUICAO,
                                                "Nova tarefa atribuída",
                                                (remetente != null ? remetente.getNome() : "Alguém") +
                                                        " atribuiu você à tarefa '" + tarefa.getTitulo() + "'",
                                                usuario,      // Destinatário
                                                remetente,    // Quem atribuiu
                                                tarefa        // Tarefa
                                        );
                                        System.out.println("📬 Notificação enviada para: " + usuario.getNome());
                                    } catch (Exception e) {
                                        System.err.println("⚠️ Erro ao enviar notificação: " + e.getMessage());
                                    }
                                }
                            }

                            System.out.println("💾 Membros atualizados com sucesso!");
                        }

                        // Salvar tarefa (primeira vez)
                        Tarefa tarefaSalva = tarefaRepository.save(tarefa);

                        // ✅ RENOMEAR PASTA NO DROPBOX SE O TÍTULO MUDOU
                        if (atualizarDTO.titulo() != null &&
                                !atualizarDTO.titulo().equals(tituloAntigo) &&
                                dropboxPathAntigo != null &&
                                !dropboxPathAntigo.isEmpty()) {
                            try {
                                Empresa empresa = tarefaSalva.getEmpresa();
                                String empresaPasta = sanitizeName(empresa.getNome());
                                String novaTarefaPasta = "Tarefa-" + tarefaSalva.getId() + "-" + sanitizeName(tarefaSalva.getTitulo());
                                String novoPath = "/" + empresaPasta + "/" + novaTarefaPasta;

                                // Renomeia a pasta no Dropbox
                                dropboxService.renameFile(dropboxPathAntigo, novoPath);

                                // Atualiza o path no banco
                                tarefaSalva.setDropboxPath(novoPath);
                                tarefaSalva = tarefaRepository.save(tarefaSalva); // Salva novamente com o novo path

                                System.out.println("📝 Pasta renomeada no Dropbox:");
                                System.out.println("   De: " + dropboxPathAntigo);
                                System.out.println("   Para: " + novoPath);
                            } catch (Exception e) {
                                System.err.println("⚠️ Erro ao renomear pasta no Dropbox: " + e.getMessage());
                                e.printStackTrace();
                                // Não quebra a atualização se der erro no Dropbox
                            }
                        }
                        // ✅ SINCRONIZAR COM GOOGLE CALENDAR SE DATA DE ENTREGA FOI ADICIONADA OU ALTERADA
                        if (dataDeEntregaFoiAdicionada || (atualizarDTO.dtEntrega() != null && !atualizarDTO.dtEntrega().equals(dataEntregaAntiga))) {
                            try {
                                String emailUsuarioLogado = segurancaUtils.getUsuarioLogadoEmail();
                                Usuario usuarioLogado = usuarioRepository.findByEmail(emailUsuarioLogado).orElse(null);

                                if (usuarioLogado != null && usuarioLogado.getGoogleCalendarConectado() && tarefaSalva.getDtEntrega() != null) {
                                    LocalDateTime dataEntrega = tarefaSalva.getDtEntrega();
                                    LocalDateTime dataFim = dataEntrega.plusHours(1);

                                    // ✅ VALIDAR QUE AS DATAS NÃO ESTÃO VAZIAS
                                    if (dataEntrega != null && dataFim != null) {
                                        // Se já existe evento, atualiza. Senão, cria novo
                                        if (tarefaSalva.getGoogleEventId() != null && !tarefaSalva.getGoogleEventId().isEmpty()) {
                                            try {
                                                googleCalendarService.atualizarEvento(
                                                        usuarioLogado,
                                                        tarefaSalva.getGoogleEventId(),
                                                        "📋 " + tarefaSalva.getTitulo(),
                                                        "Vencimento de tarefa - " + tarefaSalva.getEmpresa().getNome() +
                                                                (tarefaSalva.getDescricao() != null ? "\n\n" + tarefaSalva.getDescricao() : ""),
                                                        dataEntrega,
                                                        dataFim
                                                );
                                                System.out.println("✅ Evento atualizado no Google Calendar: " + tarefaSalva.getTitulo());
                                            } catch (Exception e) {
                                                System.err.println("⚠️ Erro ao atualizar evento (talvez foi deletado no Google). Criando novo...");
                                                // Se falhou ao atualizar, cria um novo
                                                com.google.api.services.calendar.model.Event evento = googleCalendarService.criarEvento(
                                                        usuarioLogado,
                                                        "📋 " + tarefaSalva.getTitulo(),
                                                        "Vencimento de tarefa - " + tarefaSalva.getEmpresa().getNome() +
                                                                (tarefaSalva.getDescricao() != null ? "\n\n" + tarefaSalva.getDescricao() : ""),
                                                        dataEntrega,
                                                        dataFim
                                                );

                                                tarefaSalva.setGoogleEventId(evento.getId());
                                                tarefaRepository.save(tarefaSalva);
                                            }
                                        } else {
                                            com.google.api.services.calendar.model.Event evento = googleCalendarService.criarEvento(
                                                    usuarioLogado,
                                                    "📋 " + tarefaSalva.getTitulo(),
                                                    "Vencimento de tarefa - " + tarefaSalva.getEmpresa().getNome() +
                                                            (tarefaSalva.getDescricao() != null ? "\n\n" + tarefaSalva.getDescricao() : ""),
                                                    dataEntrega,
                                                    dataFim
                                            );

                                            // Salvar o ID do evento do Google
                                            tarefaSalva.setGoogleEventId(evento.getId());
                                            tarefaRepository.save(tarefaSalva);

                                            System.out.println("✅ Tarefa sincronizada com Google Calendar: " + tarefaSalva.getTitulo());
                                        }
                                    } else {
                                        System.err.println("⚠️ Data de entrega inválida, não sincronizando com Google Calendar");
                                    }
                                }
                            } catch (Exception e) {
                                System.err.println("⚠️ Erro ao sincronizar com Google Calendar: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }




                        BuscaTarefa tarefaAtualizada = converterParaDTO(tarefaSalva);

                        // ✅ ✅ ✅ ENVIA EVENTO WEBSOCKET ✅ ✅ ✅
                        try {
                            TarefaEventoDTO evento = new TarefaEventoDTO();
                            evento.setTipo("ATUALIZADA"); // ✅ STRING
                            evento.setTarefaId(tarefaSalva.getId());
                            evento.setListaId(tarefaAtualizada.listaId());
                            evento.setTarefa(tarefaAtualizada);

                            messagingTemplate.convertAndSend("/topic/tarefas", evento);
                            System.out.println("📡 Evento WebSocket enviado: ATUALIZADA");
                        } catch (Exception e) {
                            System.err.println("⚠️ Erro ao enviar evento WebSocket: " + e.getMessage());
                            // Não falha a operação se o WebSocket der erro
                        }
                        // ✅ ✅ ✅ FIM DO WEBSOCKET ✅ ✅ ✅

                        return tarefaAtualizada;
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

        return tarefaRepository.findById(id)
                .map(tarefa -> {
                    // ✅ NOVO: Guarda a lista antiga ANTES de mover
                    Long listaIdOrigem = tarefa.getLista() != null ? tarefa.getLista().getId() : null;

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

                    BuscaTarefa tarefaDTO = converterParaDTO(tarefa);

                    // ✅ ATUALIZADO: Evento WebSocket com listaIdOrigem
                    TarefaEventoDTO evento = new TarefaEventoDTO(
                            "MOVIDA",
                            id,
                            moverDTO.novoListaId(),
                            listaIdOrigem,  // ✅ NOVO: Lista de origem
                            moverDTO.novaPosicao().intValue(),
                            tarefaDTO,
                            usuarioLogado.getNome()
                    );
                    messagingTemplate.convertAndSend("/topic/tarefas", evento);

                    return tarefaDTO;
                });
    }




    public boolean deletarTarefa(Long id) {
        try {
            if (tarefaRepository.existsById(id)) {
                tarefaRepository.deleteById(id);

                // ✅ WEBSOCKET: Enviar evento de tarefa deletada
                TarefaEventoDTO evento = new TarefaEventoDTO(
                        "DELETADA",
                        id,
                        null,
                        null,
                        null,
                        null,
                        "Sistema"
                );
                messagingTemplate.convertAndSend("/topic/tarefas", evento);

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

    // ✅ OTIMIZADO - USA findByListaIdWithDetails()
    public List<BuscaTarefa> getTarefasPorLista(Long listaId) {
        try {
            if (!listaRepository.existsById(listaId)) {
                throw new TarefaValidacaoException("Lista não encontrada com ID: " + listaId);
            }

            return tarefaRepository.findByListaIdWithDetails(listaId).stream()
                    .map(this::converterParaDTO)
                    .toList();

        } catch (TarefaValidacaoException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tarefas por lista: " + e.getMessage(), e);
        }
    }

    private BuscaTarefa converterParaDTO(Tarefa tarefa) {
        try {
            List<Membro> membros = membroRepository.findByTarefaId(tarefa.getId());

            List<Long> membroIds = membros.stream()
                    .map(Membro::getId)
                    .collect(Collectors.toList());

            List<Long> usuarioIds = membros.stream()
                    .map(membro -> membro.getUsuario().getId())
                    .collect(Collectors.toList());

            List<MembroSimplificadoDTO> membrosDTO = membros.stream()
                    .map(membro -> new MembroSimplificadoDTO(
                            membro.getId(),
                            membro.getUsuario().getId(),
                            membro.getUsuario().getNome(),
                            membro.getUsuario().getEmail(),
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
                    tarefa.getConcluida(),
                    tarefa.getTags() != null ? tarefa.getTags() : List.of(),
                    tarefa.getObservacoes(),
                    membroIds,
                    usuarioIds,
                    membrosDTO,
                    tarefa.getDropboxPath()  // ✅ ADICIONADO
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
                            tarefa.getConcluida(),
                            tarefa.getTags(),
                            anexosDTO,
                            checklistsDTO,
                            comentariosDTO,
                            membrosDTO,  // ✅ MEMBROS COM USERNAME
                            null,        // historico
                            tarefa.getObservacoes(),
                            tarefa.getDropboxPath()
                    );
                });
    }
    @Transactional
    public BuscaTarefa marcarComoConcluida(Long id, Boolean concluida) {
        System.out.println(concluida ? "✅ Marcando tarefa como concluída ID: " + id
                : "❌ Desmarcando tarefa ID: " + id);

        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        tarefa.setConcluida(concluida);
        Tarefa tarefaSalva = tarefaRepository.save(tarefa);

        BuscaTarefa tarefaDTO = converterParaDTO(tarefaSalva);

        // ✅ WEBSOCKET: Enviar evento de atualização
        try {
            String emailUsuarioLogado = segurancaUtils.getUsuarioLogadoEmail();
            Usuario usuarioLogado = usuarioRepository.findByEmail(emailUsuarioLogado).orElse(null);

            TarefaEventoDTO evento = new TarefaEventoDTO(
                    "ATUALIZADA",
                    tarefaSalva.getId(),
                    tarefaDTO.listaId(),
                    null,
                    null,
                    tarefaDTO,
                    usuarioLogado != null ? usuarioLogado.getNome() : "Sistema"
            );
            messagingTemplate.convertAndSend("/topic/tarefas", evento);
            System.out.println("📡 Evento WebSocket enviado: TAREFA CONCLUÍDA");
        } catch (Exception e) {
            System.err.println("⚠️ Erro ao enviar evento WebSocket: " + e.getMessage());
        }

        System.out.println(concluida ? "✅ Tarefa marcada como concluída!"
                : "❌ Tarefa desmarcada!");
        return tarefaDTO;
    }

}
