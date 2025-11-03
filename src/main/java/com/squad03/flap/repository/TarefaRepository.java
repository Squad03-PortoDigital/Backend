package com.squad03.flap.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.squad03.flap.model.Lista;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.squad03.flap.model.Tarefa;
import com.squad03.flap.model.Tarefa.StatusTarefa;
import com.squad03.flap.model.Tarefa.PrioridadeTarefa;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    // ✅ MÉTODO OTIMIZADO - CARREGA TUDO DE UMA VEZ (resolve o N+1)
    @EntityGraph(attributePaths = {"membros", "membros.usuario", "empresa", "lista", "tags"})
    @Query("SELECT DISTINCT t FROM Tarefa t ORDER BY t.posicao ASC")
    List<Tarefa> findAllWithDetails();

    // ✅ MÉTODO OTIMIZADO POR LISTA
    @EntityGraph(attributePaths = {"membros", "membros.usuario", "empresa", "lista", "tags"})
    @Query("SELECT DISTINCT t FROM Tarefa t WHERE t.lista.id = :listaId ORDER BY t.posicao ASC")
    List<Tarefa> findByListaIdWithDetails(@Param("listaId") Long listaId);

    List<Tarefa> findByStatus(StatusTarefa status);

    List<Tarefa> findByStatusOrderByPosicaoAsc(StatusTarefa status);

    List<Tarefa> findByPrioridade(PrioridadeTarefa prioridade);

    List<Tarefa> findByTituloContainingIgnoreCase(String titulo);

    List<Tarefa> findByDescricaoContainingIgnoreCase(String descricao);

    List<Tarefa> findByEmpresaId(Long empresaId);

    List<Tarefa> findByDtEntregaBetween(LocalDateTime dataInicio, LocalDateTime dataFim);

    List<Tarefa> findByDtEntregaBeforeAndStatusNot(LocalDateTime data, StatusTarefa status);

    List<Tarefa> findByListaId(Long listaId);

    @Query("SELECT MAX(t.posicao) FROM Tarefa t WHERE t.lista.id = :listaId")
    Double findMaxPosicaoByListaId(@Param("listaId") Long listaId);

    @Query("SELECT t FROM Tarefa t WHERE :tag MEMBER OF t.tags")
    List<Tarefa> findByTag(@Param("tag") String tag);

    @Query("SELECT t FROM Tarefa t WHERE t.dtEntrega IS NOT NULL AND t.dtEntrega < CURRENT_TIMESTAMP AND t.status NOT IN ('CONCLUIDA', 'CANCELADA')")
    List<Tarefa> findTarefasAtrasadas();

    List<Tarefa> findByListaOrderByPosicaoAsc(Lista novaLista);
}
