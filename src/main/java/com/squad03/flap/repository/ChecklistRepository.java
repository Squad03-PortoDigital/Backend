package com.squad03.flap.repository;

import com.squad03.flap.model.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChecklistRepository extends JpaRepository<Checklist, Long> {
    
    @Query("SELECT c FROM Checklist c WHERE c.tarefa.id = :tarefaId")
    List<Checklist> findByTarefaId(@Param("tarefaId") Long tarefaId);
    
    @Query("SELECT c FROM Checklist c LEFT JOIN FETCH c.itens WHERE c.id = :id")
    Checklist findByIdWithItens(@Param("id") Long id);
    
    @Query("SELECT c FROM Checklist c LEFT JOIN FETCH c.itens WHERE c.tarefa.id = :tarefaId")
    List<Checklist> findByTarefaIdWithItens(@Param("tarefaId") Long tarefaId);
}