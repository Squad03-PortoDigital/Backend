package com.squad03.flap.model;

import com.squad03.flap.DTO.CadastroEmpresa;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;

    private String foto;

    @OneToMany(mappedBy = "empresa")
    private List<Tarefa> tarefa;

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

    public List<Tarefa> getTarefa() {
        return tarefa;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Empresa empresa)) return false;

        return id == empresa.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
