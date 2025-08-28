package com.squad03.flap.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Quadro")
public class Quadro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;
    private String status;
    private String foto;

    @OneToMany(mappedBy = "quadro")
    private List<UsuarioQuadro> usuariosQuadros;

    @OneToMany
    private List<Agente> agentes;

    public Quadro() {
    }

    public Quadro(String foto, String status, String nome) {
        this.foto = foto;
        this.status = status;
        this.nome = nome;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Quadro quadro)) return false;

        return id == quadro.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
