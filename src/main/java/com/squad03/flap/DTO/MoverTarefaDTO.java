package com.squad03.flap.DTO;

public record MoverTarefaDTO(
        Double posicaoVizinhoAnterior,
        Double posicaoVizinhoPosterior,
        Long novoListaId
) {}
