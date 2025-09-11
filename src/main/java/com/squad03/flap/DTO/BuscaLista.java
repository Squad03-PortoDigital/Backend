package com.squad03.flap.DTO;

import com.squad03.flap.model.Lista;

public record BuscaLista(String nome, int posicao, String cor) {
    public BuscaLista (Lista lista){
        this(lista.getNome(), lista.getPosicao(), lista.getCor());
    }
}
