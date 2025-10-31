package com.squad03.flap.DTO;

public record MoverTarefaDTO(
        Double posicaoVizinhoAnterior, // pode não ser usado mais, mas mantenha se quiser
        Double posicaoVizinhoPosterior,
        Long novoListaId,
        Integer novaPosicao // novo campo que representa o índice exato
) {}

