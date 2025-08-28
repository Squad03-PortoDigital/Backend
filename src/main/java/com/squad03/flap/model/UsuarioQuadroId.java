package com.squad03.flap.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class UsuarioQuadroId {

    private int UsuarioId;

    private int QuadroId;

    public UsuarioQuadroId() {
    }

    public UsuarioQuadroId(int usuarioId, int quadroId) {
        UsuarioId = usuarioId;
        QuadroId = quadroId;
    }

    public int getUsuarioId() {
        return UsuarioId;
    }

    public int getQuadroId() {
        return QuadroId;
    }
}
