package com.squad03.flap.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Cargo")
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;

    @OneToMany
    private List<Usuario> usuario;

    public Cargo() {
    }

    public Cargo(String nome, List<Usuario> usuario) {
        this.nome = nome;
        this.usuario = usuario;
    }

    public long getId() {
        return id;
    }

    public List<Usuario> getUsuario() {
        return usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Cargo cargo)) return false;

        return id == cargo.id && Objects.equals(nome, cargo.nome);
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(id);
        result = 31 * result + Objects.hashCode(nome);
        return result;
    }
}
