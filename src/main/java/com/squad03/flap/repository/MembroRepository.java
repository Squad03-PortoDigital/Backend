package com.squad03.flap.repository;

import com.squad03.flap.model.Membro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembroRepository extends JpaRepository<Membro, Long> {

    List<Membro> findByUsuarioId(Long usuarioId);

    List<Membro> findByTarefaId(Long tarefaId);

    @Query("SELECT m FROM Membro m WHERE m.usuario.id = :usuarioId AND m.tarefa.id = :tarefaId")
    Optional<Membro> findByUsuarioIdAndTarefaId(@Param("usuarioId") Long usuarioId,
                                                @Param("tarefaId") Long tarefaId);

    boolean existsByUsuarioIdAndTarefaId(Long usuarioId, Long tarefaId);

    void deleteByTarefaId(Long tarefaId);

    void deleteByUsuarioId(Long usuarioId);
}