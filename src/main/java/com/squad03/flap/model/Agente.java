package com.squad03.flap.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Agente")
public class Agente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;
    private String link;
    private String foto;

    @ManyToOne
    @JoinColumn(name = "quadro_id")
    private Quadro quadro;

    public Agente() {
    }

    public Agente(String nome, String link, String foto, Quadro quadro) {
        this.nome = nome;
        this.link = link;
        this.foto = foto;
        this.quadro = quadro;
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

    public Quadro getQuadro() {
        return quadro;
    }

    public void setQuadro(Quadro quadro) {
        this.quadro = quadro;
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
