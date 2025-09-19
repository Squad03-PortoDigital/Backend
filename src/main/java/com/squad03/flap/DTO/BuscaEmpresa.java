package com.squad03.flap.DTO;

import com.squad03.flap.model.Empresa;

public record BuscaEmpresa(Long id, String nome, String cnpj, String email, String contato,
                           String atuacao, String observacao, String foto) {
    public BuscaEmpresa(Empresa empresa) {

        this(empresa.getId(), empresa.getNome(), empresa.getCnpj(), empresa.getEmail(),
                empresa.getContato(), empresa.getAtuacao(), empresa.getObservacao(), empresa.getFoto());
    }
}
