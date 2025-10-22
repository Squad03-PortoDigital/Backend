package com.squad03.flap.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "permissao")
public class Permissao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @ManyToMany(mappedBy = "permissoes")
    private List<Role> roles;

    public Permissao() {
    }

    public Permissao(String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
