package com.squad03.flap.repository;

import com.squad03.flap.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    
    @Query("SELECT i FROM Item i WHERE i.tarefa.id = :tarefaId")
    List<Item> findByTarefaId(@Param("tarefaId") Long tarefaId);
    
    @Query("SELECT i FROM Item i WHERE i.tarefa.id = :tarefaId AND i.status = :status")
    List<Item> findByTarefaIdAndStatus(@Param("tarefaId") Long tarefaId, @Param("status") Boolean status);
    
    @Query("SELECT i FROM Item i WHERE i.checklist.id = :checklistId")
    List<Item> findByChecklistId(@Param("checklistId") Long checklistId);
}