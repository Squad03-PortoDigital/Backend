package com.squad03.flap.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tarefa")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agente_id")
    private Agente agente;

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
    private Integer posicao;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime dtCriacao;
    
    private LocalDateTime dtEntrega;
    
    private LocalDateTime dtConclusao;
    
    @ElementCollection
    @CollectionTable(name = "tarefa_tags", joinColumns = @JoinColumn(name = "tarefa_id"))
    @Column(name = "tag")
    private List<String> tags;
    
    @Column(length = 500)
    private String observacoes;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "lista_id")
    private Lista lista;

    @OneToMany
    private Set<Anexo> anexos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Agente getAgente() {
        return agente;
    }

    public void setAgente(Agente agente) {
        this.agente = agente;
    }

    public Set<Anexo> getAnexos() {
        return anexos;
    }

    public void setAnexos(Set<Anexo> anexos) {
        this.anexos = anexos;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Lista getLista() {
        return lista;
    }

    public void setLista(Lista lista) {
        this.lista = lista;
    }

    @PrePersist
    protected void onCreate() {
        dtCriacao = LocalDateTime.now();
        if (posicao == null) {
            posicao = 0;
        }
        if (status == null) {
            status = StatusTarefa.A_FAZER;
        }
        if (prioridade == null) {
            prioridade = PrioridadeTarefa.MEDIA;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if (status == StatusTarefa.CONCLUIDA && dtConclusao == null) {
            dtConclusao = LocalDateTime.now();
        }
    }

    public enum StatusTarefa {
        A_FAZER("A Fazer"),
        EM_PROGRESSO("Em Progresso"),
        EM_REVISAO("Em Revisão"),
        CONCLUIDA("Concluída"),
        CANCELADA("Cancelada");

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
}