package com.squad03.flap.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Agente")
public class Agente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;
    private String link;
    private String foto;

    @OneToMany(mappedBy = "agente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Tarefa> tarefas = new HashSet<>();

    public Agente() {
    }

    public Agente(String nome, String link, String foto) {
        this.nome = nome;
        this.link = link;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Set<Tarefa> getTarefas() { return tarefas; }

    public void setTarefas(Set<Tarefa> tarefas) {this.tarefas = tarefas; }

    public void adicionarTarefa(Tarefa tarefa) {
        this.tarefas.add(tarefa);
        tarefa.setAgente(this);
    }

    public void removerTarefa(Tarefa tarefa) {
        this.tarefas.remove(tarefa);
        tarefa.setAgente(null);
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Agente agente)) return false;

        return id == agente.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
