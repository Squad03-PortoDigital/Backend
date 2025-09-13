package com.squad03.flap.DTO;

import com.squad03.flap.model.Lista;

public record BuscaLista(int id, String nome, int posicao, String cor) {
    public BuscaLista (Lista lista){
        this(lista.getId(), lista.getNome(), lista.getPosicao(), lista.getCor());
    }
}
