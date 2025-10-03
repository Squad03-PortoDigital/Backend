package com.squad03.flap.model;

import java.time.LocalDateTime;
import java.util.HashSet;
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
    @JoinColumn(name = "agente_id", nullable = false)
    private Agente agente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lista_id", nullable = false)
    private Lista lista;

    @OneToMany(mappedBy = "tarefa", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Anexo> anexos = new HashSet<>();

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

    @PrePersist
    protected void onCreate() {
        if (dtCriacao == null) {
            dtCriacao = LocalDateTime.now();
        }
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