package com.squad03.flap.model;

import com.squad03.flap.DTO.CadastroItem;
import jakarta.persistence.*;

@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(nullable = false)
    private Boolean status = false; // false = não concluído, true = concluído

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarefa_id", nullable = false)
    private Tarefa tarefa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checklist_id")
    private Checklist checklist;

    public Item(String nome, Tarefa tarefa) {
        this.nome = nome;
        this.status = false;
        this.tarefa = tarefa;
    }

    public Item(CadastroItem cadastroItem, Tarefa tarefa) {
        this.nome = cadastroItem.nome();
        this.status = cadastroItem.status() != null ? cadastroItem.status() : false;
        this.tarefa = tarefa;
    }

    public Item(Long id, String nome, Boolean status, Tarefa tarefa, Checklist checklist) {
        this.id = id;
        this.nome = nome;
        this.status = status;
        this.tarefa = tarefa;
        this.checklist = checklist;
    }

    public Item() {
    }

    private static Boolean $default$status() {
        return false;
    }

    public static ItemBuilder builder() {
        return new ItemBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Tarefa getTarefa() {
        return tarefa;
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
    }

    public Checklist getChecklist() {
        return checklist;
    }

    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    public static class ItemBuilder {
        private Long id;
        private String nome;
        private Boolean status$value;
        private boolean status$set;
        private Tarefa tarefa;
        private Checklist checklist;

        ItemBuilder() {
        }

        public ItemBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ItemBuilder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public ItemBuilder status(Boolean status) {
            this.status$value = status;
            this.status$set = true;
            return this;
        }

        public ItemBuilder tarefa(Tarefa tarefa) {
            this.tarefa = tarefa;
            return this;
        }

        public ItemBuilder checklist(Checklist checklist) {
            this.checklist = checklist;
            return this;
        }

        public Item build() {
            Boolean status$value = this.status$value;
            if (!this.status$set) {
                status$value = Item.$default$status();
            }
            return new Item(this.id, this.nome, status$value, this.tarefa, this.checklist);
        }

        public String toString() {
            return "Item.ItemBuilder(id=" + this.id + ", nome=" + this.nome + ", status$value=" + this.status$value + ", tarefa=" + this.tarefa + ", checklist=" + this.checklist + ")";
        }
    }
}