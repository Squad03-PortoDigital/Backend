package com.squad03.flap.DTO;

import com.squad03.flap.model.Lista;
import com.squad03.flap.model.Movimentacao;
import com.squad03.flap.model.Role;

public record
BuscaMovimentacao(Long id, Role role_id, Lista listaOrigem_id, Lista listaDestino_id) {
    public BuscaMovimentacao (Movimentacao movimentacao){
        this(movimentacao.getId(), movimentacao.getRole(), movimentacao.getListaOrigem(),
                movimentacao.getListaDestino());
    }
}
