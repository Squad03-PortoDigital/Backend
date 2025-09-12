package com.squad03.flap.DTO;

import com.squad03.flap.model.Agente;

public record BuscaAgente(
        int id,
        String nome,
        String link,
        String foto) {

    public BuscaAgente(Agente agente){
        this(
                agente.getId(),
                agente.getNome(),
                agente.getLink(),
                agente.getFoto()
        );
    }
}
