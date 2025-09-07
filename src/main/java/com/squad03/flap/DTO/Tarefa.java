package com.squad03.flap.DTO;

public record Tarefa(int id, int agente_id, String nome, String titulo, String descricao, String posicao, String dt_Criacao,
        String dt_Entrega) {
}
