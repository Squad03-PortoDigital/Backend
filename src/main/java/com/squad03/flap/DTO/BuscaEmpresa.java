package com.squad03.flap.DTO;

import com.squad03.flap.model.Empresa;

public record BuscaEmpresa(String nome, String foto) {
    public BuscaEmpresa(Empresa empresa) {
        this(empresa.getNome(), empresa.getFoto());
    }
}
