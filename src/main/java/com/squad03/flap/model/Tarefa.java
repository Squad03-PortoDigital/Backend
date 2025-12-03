package com.squad03.flap.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "tarefa")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lista_id", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Lista lista;

    @OneToMany(mappedBy = "tarefa", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"tarefa"})
    private Set<Anexo> anexos = new HashSet<>();

    @OneToMany(mappedBy = "tarefa", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"tarefa"})
    private Set<Checklist> checklists = new HashSet<>();

    @OneToMany(mappedBy = "tarefa", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"tarefa"})
    private Set<Comentario> comentarios = new HashSet<>();

    @OneToMany(mappedBy = "tarefa", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"tarefa"})
    private Set<Membro> membros = new HashSet<>();

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTarefa status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrioridadeTarefa prioridade;

    @Column(nullable = false)
    private Double posicao;

    @Column(name = "google_event_id", length = 500)
    private String googleEventId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dtCriacao;

    private LocalDateTime dtEntrega;

    private LocalDateTime dtConclusao;

    @Column(nullable = false)
    private Boolean concluida = false;  // ✅ NOVO CAMPO

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tarefa_tags", joinColumns = @JoinColumn(name = "tarefa_id"))
    @Column(name = "tag")
    private List<String> tags;

    @Column(length = 500)
    private String observacoes;

    @Column(name = "dropbox_path", length = 500)
    private String dropboxPath;

    public Tarefa(Long id, Empresa empresa, Lista lista, Set<Anexo> anexos, Set<Checklist> checklists, Set<Comentario> comentarios, Set<Membro> membros, String titulo, String descricao, StatusTarefa status, PrioridadeTarefa prioridade, Double posicao, LocalDateTime dtCriacao, LocalDateTime dtEntrega, LocalDateTime dtConclusao, Boolean concluida, List<String> tags, String observacoes, String dropboxPath) {
        this.id = id;
        this.empresa = empresa;
        this.lista = lista;
        this.anexos = anexos;
        this.checklists = checklists;
        this.comentarios = comentarios;
        this.membros = membros;
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.prioridade = prioridade;
        this.posicao = posicao;
        this.dtCriacao = dtCriacao;
        this.dtEntrega = dtEntrega;
        this.dtConclusao = dtConclusao;
        this.concluida = concluida;
        this.tags = tags;
        this.observacoes = observacoes;
        this.dropboxPath = dropboxPath;
    }

    public Tarefa() {
    }

    public static TarefaBuilder builder() {
        return new TarefaBuilder();
    }

    @PrePersist
    protected void onCreate() {
        if (dtCriacao == null) {
            dtCriacao = LocalDateTime.now();
        }
        if (posicao == null) {
            posicao = 0.0;
        }
        if (status == null) {
            status = StatusTarefa.A_FAZER;
        }
        if (prioridade == null) {
            prioridade = PrioridadeTarefa.MEDIA;
        }
        if (concluida == null) {
            concluida = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if (status == StatusTarefa.CONCLUIDA && dtConclusao == null) {
            dtConclusao = LocalDateTime.now();
        }
    }

    public Long getId() {
        return this.id;
    }

    public Empresa getEmpresa() {
        return this.empresa;
    }

    public Lista getLista() {
        return this.lista;
    }

    public Set<Anexo> getAnexos() {
        return this.anexos;
    }

    public Set<Checklist> getChecklists() {
        return this.checklists;
    }

    public Set<Comentario> getComentarios() {
        return this.comentarios;
    }

    public Set<Membro> getMembros() {
        return this.membros;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public StatusTarefa getStatus() {
        return this.status;
    }

    public PrioridadeTarefa getPrioridade() {
        return this.prioridade;
    }

    public Double getPosicao() {
        return this.posicao;
    }

    public LocalDateTime getDtCriacao() {
        return this.dtCriacao;
    }

    public LocalDateTime getDtEntrega() {
        return this.dtEntrega;
    }

    public LocalDateTime getDtConclusao() {
        return this.dtConclusao;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public String getObservacoes() {
        return this.observacoes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public void setLista(Lista lista) {
        this.lista = lista;
    }

    @JsonIgnoreProperties({"tarefa"})
    public void setAnexos(Set<Anexo> anexos) {
        this.anexos = anexos;
    }

    @JsonIgnoreProperties({"tarefa"})
    public void setChecklists(Set<Checklist> checklists) {
        this.checklists = checklists;
    }

    @JsonIgnoreProperties({"tarefa"})
    public void setComentarios(Set<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    @JsonIgnoreProperties({"tarefa"})
    public void setMembros(Set<Membro> membros) {
        this.membros = membros;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setStatus(StatusTarefa status) {
        this.status = status;
    }

    public void setPrioridade(PrioridadeTarefa prioridade) {
        this.prioridade = prioridade;
    }

    public void setPosicao(Double posicao) {
        this.posicao = posicao;
    }

    public void setDtCriacao(LocalDateTime dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public void setDtEntrega(LocalDateTime dtEntrega) {
        this.dtEntrega = dtEntrega;
    }

    public void setDtConclusao(LocalDateTime dtConclusao) {
        this.dtConclusao = dtConclusao;
    }

    public Boolean getConcluida() {
        return concluida;
    }

    public void setConcluida(Boolean concluida) {
        this.concluida = concluida;
        // ✅ Atualiza automaticamente a data de conclusão
        if (concluida && this.dtConclusao == null) {
            this.dtConclusao = LocalDateTime.now();
        }
        // Se desmarcou, limpa a data
        if (!concluida) {
            this.dtConclusao = null;
        }
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getDropboxPath() {
        return dropboxPath;
    }

    public void setDropboxPath(String dropboxPath) {
        this.dropboxPath = dropboxPath;
    }

    public String getGoogleEventId() {
        return googleEventId;
    }

    public void setGoogleEventId(String googleEventId) {
        this.googleEventId = googleEventId;
    }


    // ✅ ENUM ATUALIZADO COM ARQUIVADA
    public enum StatusTarefa {
        A_FAZER("A Fazer"),
        EM_PROGRESSO("Em Progresso"),
        EM_REVISAO("Em Revisão"),
        CONCLUIDA("Concluída"),
        CANCELADA("Cancelada"),
        ARQUIVADA("Arquivada");  // ✅ ADICIONADO

        private final String descricao;

        StatusTarefa(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    public enum PrioridadeTarefa {
        BAIXA("Baixa"),
        MEDIA("Média"),
        ALTA("Alta"),
        CRITICA("Crítica");

        private final String descricao;

        PrioridadeTarefa(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    public static class TarefaBuilder {
        private Long id;
        private Empresa empresa;
        private Lista lista;
        private Set<Anexo> anexos;
        private Set<Checklist> checklists;
        private Set<Comentario> comentarios;
        private Set<Membro> membros;
        private String titulo;
        private String descricao;
        private StatusTarefa status;
        private PrioridadeTarefa prioridade;
        private Double posicao;
        private LocalDateTime dtCriacao;
        private LocalDateTime dtEntrega;
        private LocalDateTime dtConclusao;
        private List<String> tags;
        private String observacoes;
        private Boolean concluida;
        private String dropboxPath;

        TarefaBuilder() {
        }

        public TarefaBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
        public TarefaBuilder empresa(Empresa empresa) {
            this.empresa = empresa;
            return this;
        }

        @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
        public TarefaBuilder lista(Lista lista) {
            this.lista = lista;
            return this;
        }

        @JsonIgnoreProperties({"tarefa"})
        public TarefaBuilder anexos(Set<Anexo> anexos) {
            this.anexos = anexos;
            return this;
        }

        @JsonIgnoreProperties({"tarefa"})
        public TarefaBuilder checklists(Set<Checklist> checklists) {
            this.checklists = checklists;
            return this;
        }

        @JsonIgnoreProperties({"tarefa"})
        public TarefaBuilder comentarios(Set<Comentario> comentarios) {
            this.comentarios = comentarios;
            return this;
        }

        @JsonIgnoreProperties({"tarefa"})
        public TarefaBuilder membros(Set<Membro> membros) {
            this.membros = membros;
            return this;
        }

        public TarefaBuilder titulo(String titulo) {
            this.titulo = titulo;
            return this;
        }

        public TarefaBuilder descricao(String descricao) {
            this.descricao = descricao;
            return this;
        }

        public TarefaBuilder status(StatusTarefa status) {
            this.status = status;
            return this;
        }

        public TarefaBuilder prioridade(PrioridadeTarefa prioridade) {
            this.prioridade = prioridade;
            return this;
        }

        public TarefaBuilder posicao(Double posicao) {
            this.posicao = posicao;
            return this;
        }

        public TarefaBuilder dtCriacao(LocalDateTime dtCriacao) {
            this.dtCriacao = dtCriacao;
            return this;
        }

        public TarefaBuilder dtEntrega(LocalDateTime dtEntrega) {
            this.dtEntrega = dtEntrega;
            return this;
        }

        public TarefaBuilder dtConclusao(LocalDateTime dtConclusao) {
            this.dtConclusao = dtConclusao;
            return this;
        }

        public TarefaBuilder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }

        public TarefaBuilder observacoes(String observacoes) {
            this.observacoes = observacoes;
            return this;
        }
        public TarefaBuilder concluida(Boolean concluida) {
            this.concluida = concluida;
            return this;
        }

        public TarefaBuilder dropboxPath(String dropboxPath) {  // ✅ NOVO MÉTODO
            this.dropboxPath = dropboxPath;
            return this;
        }

        public Tarefa build() {
            return new Tarefa(this.id, this.empresa, this.lista, this.anexos, this.checklists, this.comentarios, this.membros, this.titulo, this.descricao, this.status, this.prioridade, this.posicao, this.dtCriacao, this.dtEntrega, this.dtConclusao, this.concluida, this.tags, this.observacoes, this.dropboxPath);
        }

        public String toString() {
            return "Tarefa.TarefaBuilder(id=" + this.id + ", empresa=" + this.empresa + ", lista=" + this.lista + ", anexos=" + this.anexos + ", checklists=" + this.checklists + ", comentarios=" + this.comentarios + ", membros=" + this.membros + ", titulo=" + this.titulo + ", descricao=" + this.descricao + ", status=" + this.status + ", prioridade=" + this.prioridade + ", posicao=" + this.posicao + ", dtCriacao=" + this.dtCriacao + ", dtEntrega=" + this.dtEntrega + ", dtConclusao=" + this.dtConclusao + ", tags=" + this.tags + ", observacoes=" + this.observacoes + ")";
        }
    }
}