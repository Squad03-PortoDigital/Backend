package com.squad03.flap.DTO;

import com.squad03.flap.model.Empresa;

import java.time.LocalDateTime;

public record BuscaEmpresa(
        Long id,
        String nome,
        String cnpj,
        String email,
        String contato,
        String atuacao,
        String observacao,
        String foto,
        String agenteLink,
        Boolean arquivada,          // ✅ NOVO
        LocalDateTime dataCriacao   // ✅ NOVO
) {
    public BuscaEmpresa(Empresa empresa) {
        this(
                empresa.getId(),
                empresa.getNome(),
                empresa.getCnpj(),
                empresa.getEmail(),
                empresa.getContato(),
                empresa.getAtuacao(),
                empresa.getObservacao(),
                empresa.getFoto(),
                empresa.getAgenteLink(),
                empresa.getArquivada(),      // ✅ NOVO
                empresa.getDataCriacao()     // ✅ NOVO
        );
    }
}
