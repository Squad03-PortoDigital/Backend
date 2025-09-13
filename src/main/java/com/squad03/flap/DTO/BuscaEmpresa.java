package com.squad03.flap.DTO;

import com.squad03.flap.model.Empresa;

public record BuscaEmpresa(int id, String nome, String foto) {
    public BuscaEmpresa(Empresa empresa) {
        this(empresa.getId(), empresa.getNome(), empresa.getFoto());
    }
}
