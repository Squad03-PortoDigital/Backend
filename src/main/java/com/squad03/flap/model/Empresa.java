package com.squad03.flap.model;

import com.squad03.flap.DTO.CadastroEmpresa;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;

    private String foto;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tarefa> tarefas = new ArrayList<>();

    public Empresa() {}

    public Empresa(String nome, String foto) {
        this.nome = nome;
        this.foto = foto;
    }

    public Empresa(CadastroEmpresa cadastroEmpresa) {
        this.nome = cadastroEmpresa.nome();
        this.foto = cadastroEmpresa.foto();
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Empresa empresa)) return false;
        return id == empresa.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
