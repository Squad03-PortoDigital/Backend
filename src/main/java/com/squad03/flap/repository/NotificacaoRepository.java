package com.squad03.flap.repository;

import com.squad03.flap.model.Notificacao;
import com.squad03.flap.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    // ✅ ATUALIZADO: Eager fetch de Tarefa e Remetente
    @Query("SELECT n FROM Notificacao n " +
            "LEFT JOIN FETCH n.tarefa " +
            "LEFT JOIN FETCH n.remetente " +
            "WHERE n.usuario = :usuario " +
            "ORDER BY n.dataHora DESC")
    List<Notificacao> findByUsuarioOrderByDataHoraDesc(@Param("usuario") Usuario usuario);

    @Query("SELECT n FROM Notificacao n " +
            "LEFT JOIN FETCH n.tarefa " +
            "LEFT JOIN FETCH n.remetente " +
            "WHERE n.usuario = :usuario AND n.lida = :lida " +
            "ORDER BY n.dataHora DESC")
    List<Notificacao> findByUsuarioAndLidaOrderByDataHoraDesc(
            @Param("usuario") Usuario usuario,
            @Param("lida") Boolean lida
    );

    Long countByUsuarioAndLida(Usuario usuario, Boolean lida);
}
