package com.squad03.flap.model;

import jakarta.persistence.*;

@Entity
public class UsuarioQuadro {

    @EmbeddedId
    private UsuarioQuadroId id;

    @ManyToOne
    @MapsId("UsuarioId")
    @JoinColumn(name = "UsuarioId")
    private Usuario usuario;

    @ManyToOne
    @MapsId("QuadroId")
    @JoinColumn(name = "QuadroId")
    private Quadro quadro;

    @ManyToOne
    @JoinColumn(name = "Role_id")
    private Role role;

    public UsuarioQuadro() {
    }

    public UsuarioQuadro(Usuario usuario, Quadro quadro) {
        this.usuario = usuario;
        this.quadro = quadro;
    }

    public UsuarioQuadroId getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Quadro getQuadro() {
        return quadro;
    }
}
