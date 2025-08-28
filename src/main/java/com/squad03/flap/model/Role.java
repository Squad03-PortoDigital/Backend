package com.squad03.flap.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;

    @OneToMany(mappedBy = "role")
    private List<UsuarioQuadro> UsuarioQuadros;

    public Role() {
    }

    public Role(String nome) {
        this.nome = nome;
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

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Role role)) return false;

        return id == role.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
