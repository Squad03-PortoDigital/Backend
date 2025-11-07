package com.squad03.flap.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "checklist")
public class Checklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(length = 7) // Para cores hex como #FFFFFF
    private String cor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarefa_id", nullable = false)
    private Tarefa tarefa;

    @OneToMany(mappedBy = "checklist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Item> itens;

    public Checklist(String titulo, String cor, Tarefa tarefa) {
        this.titulo = titulo;
        this.cor = cor;
        this.tarefa = tarefa;
    }

    public Checklist(Long id, String titulo, String cor, Tarefa tarefa, Set<Item> itens) {
        this.id = id;
        this.titulo = titulo;
        this.cor = cor;
        this.tarefa = tarefa;
        this.itens = itens;
    }

    public Checklist() {
    }

    public static ChecklistBuilder builder() {
        return new ChecklistBuilder();
    }

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

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Tarefa getTarefa() {
        return tarefa;
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
    }

    public Set<Item> getItens() {
        return itens;
    }

    public void setItens(Set<Item> itens) {
        this.itens = itens;
    }

    public static class ChecklistBuilder {
        private Long id;
        private String titulo;
        private String cor;
        private Tarefa tarefa;
        private Set<Item> itens;

        ChecklistBuilder() {
        }

        public ChecklistBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ChecklistBuilder titulo(String titulo) {
            this.titulo = titulo;
            return this;
        }

        public ChecklistBuilder cor(String cor) {
            this.cor = cor;
            return this;
        }

        public ChecklistBuilder tarefa(Tarefa tarefa) {
            this.tarefa = tarefa;
            return this;
        }

        public ChecklistBuilder itens(Set<Item> itens) {
            this.itens = itens;
            return this;
        }

        public Checklist build() {
            return new Checklist(this.id, this.titulo, this.cor, this.tarefa, this.itens);
        }

        public String toString() {
            return "Checklist.ChecklistBuilder(id=" + this.id + ", titulo=" + this.titulo + ", cor=" + this.cor + ", tarefa=" + this.tarefa + ", itens=" + this.itens + ")";
        }
    }
}