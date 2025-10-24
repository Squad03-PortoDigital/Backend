package com.squad03.flap.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.squad03.flap.model.Tarefa;
import com.squad03.flap.model.Tarefa.StatusTarefa;
import com.squad03.flap.model.Tarefa.PrioridadeTarefa;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    
    List<Tarefa> findByStatus(StatusTarefa status);
    
    List<Tarefa> findByStatusOrderByPosicaoAsc(StatusTarefa status);
    
    List<Tarefa> findByPrioridade(PrioridadeTarefa prioridade);
    
    List<Tarefa> findByTituloContainingIgnoreCase(String titulo);
    
    List<Tarefa> findByDescricaoContainingIgnoreCase(String descricao);

    List<Tarefa> findByEmpresaId(Long empresaId);
    
    List<Tarefa> findByDtEntregaBetween(LocalDateTime dataInicio, LocalDateTime dataFim);
    
    List<Tarefa> findByDtEntregaBeforeAndStatusNot(LocalDateTime data, StatusTarefa status);





    @Query("SELECT MAX(t.posicao) FROM Tarefa t WHERE t.status = :status")
    Integer findMaxPosicaoByStatus(@Param("status") StatusTarefa status);
    
    @Query("SELECT t FROM Tarefa t WHERE :tag MEMBER OF t.tags")
    List<Tarefa> findByTag(@Param("tag") String tag);
    

    
    @Query("SELECT t FROM Tarefa t WHERE t.dtEntrega IS NOT NULL AND t.dtEntrega < CURRENT_TIMESTAMP AND t.status NOT IN ('CONCLUIDA', 'CANCELADA')")
    List<Tarefa> findTarefasAtrasadas();
}
