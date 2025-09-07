package com.squad03.flap.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.squad03.flap.model.Agente;
import com.squad03.flap.model.Tarefa;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    // List<Tarefa> findByLista(Lista lista);
    // List<Tarefa> findByEmpresa(Empresa empresa);
    List<Tarefa> findByAgente(Agente agente);
    List<Tarefa> findByNomeContaining(String nome);
    List<Tarefa> findByTituloContaining(String titulo);
    List<Tarefa> findByDescricaoContaining(String descricao);
    List<Tarefa> findByPosicao(String posicao);
    List<Tarefa> findByDt_Criacao(Date dt_Criacao);
    List<Tarefa> findByDt_Entrega(Date dt_Entrega);
}
