package com.squad03.flap.repository;

import com.squad03.flap.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    @Query("SELECT c FROM Comentario c WHERE c.tarefa.id = :tarefaId ORDER BY c.dataCriacao DESC")
    List<Comentario> findByTarefaId(@Param("tarefaId") Long tarefaId);

    @Query("SELECT c FROM Comentario c WHERE c.usuario.id = :usuarioId ORDER BY c.dataCriacao DESC")
    List<Comentario> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT c FROM Comentario c WHERE c.usuario.id = :usuarioId AND c.tarefa.id = :tarefaId ORDER BY c.dataCriacao DESC")
    List<Comentario> findByUsuarioIdAndTarefaId(@Param("usuarioId") Long usuarioId, @Param("tarefaId") Long tarefaId);
}