package com.squad03.flap.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.squad03.flap.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.squad03.flap.model.Agente;
import com.squad03.flap.model.Tarefa;
import com.squad03.flap.model.Tarefa.StatusTarefa;
import com.squad03.flap.model.Tarefa.PrioridadeTarefa;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    List<Tarefa> findByAgente(Agente agente);

    List<Tarefa> findByEmpresa(Empresa empresa);
    
    List<Tarefa> findByAgenteOrderByPosicaoAsc(Agente agente);
    
    List<Tarefa> findByStatus(StatusTarefa status);
    
    List<Tarefa> findByStatusOrderByPosicaoAsc(StatusTarefa status);
    
    List<Tarefa> findByPrioridade(PrioridadeTarefa prioridade);
    
    List<Tarefa> findByTituloContainingIgnoreCase(String titulo);
    
    List<Tarefa> findByDescricaoContainingIgnoreCase(String descricao);
    
    List<Tarefa> findByDtEntregaBetween(LocalDateTime dataInicio, LocalDateTime dataFim);
    
    List<Tarefa> findByDtEntregaBeforeAndStatusNot(LocalDateTime data, StatusTarefa status);
    
    @Query("SELECT t FROM Tarefa t WHERE t.agente = :agente AND t.status = :status ORDER BY t.posicao ASC")
    List<Tarefa> findByAgenteAndStatusOrderByPosicao(@Param("agente") Agente agente, @Param("status") StatusTarefa status);
    
    @Query("SELECT MAX(t.posicao) FROM Tarefa t WHERE t.agente = :agente AND t.status = :status")
    Integer findMaxPosicaoByAgenteAndStatus(@Param("agente") Agente agente, @Param("status") StatusTarefa status);
    
    @Query("SELECT t FROM Tarefa t WHERE t.agente = :agente AND t.status = :status AND t.posicao > :posicao ORDER BY t.posicao ASC")
    List<Tarefa> findTarefasParaReordenar(@Param("agente") Agente agente, @Param("status") StatusTarefa status, @Param("posicao") Integer posicao);
    
    @Query("SELECT t FROM Tarefa t WHERE :tag MEMBER OF t.tags")
    List<Tarefa> findByTag(@Param("tag") String tag);
    
    @Query("SELECT COUNT(t) FROM Tarefa t WHERE t.agente = :agente AND t.status = :status")
    Long countByAgenteAndStatus(@Param("agente") Agente agente, @Param("status") StatusTarefa status);
    
    @Query("SELECT t FROM Tarefa t WHERE t.dtEntrega IS NOT NULL AND t.dtEntrega < CURRENT_TIMESTAMP AND t.status NOT IN ('CONCLUIDA', 'CANCELADA')")
    List<Tarefa> findTarefasAtrasadas();
}
