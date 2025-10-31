package com.squad03.flap.repository;

import com.squad03.flap.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    // Buscar eventos por data
    List<Evento> findByData(LocalDate data);

    // Buscar eventos entre duas datas
    List<Evento> findByDataBetween(LocalDate dataInicio, LocalDate dataFim);

    // Buscar eventos por tipo
    List<Evento> findByTipo(Evento.TipoEvento tipo);

    // Buscar eventos por mês e ano
    @Query("SELECT e FROM Evento e WHERE MONTH(e.data) = :mes AND YEAR(e.data) = :ano ORDER BY e.data ASC")
    List<Evento> findByMesAndAno(@Param("mes") int mes, @Param("ano") int ano);

    // Buscar eventos futuros
    @Query("SELECT e FROM Evento e WHERE e.data >= :dataAtual ORDER BY e.data ASC")
    List<Evento> findEventosFuturos(@Param("dataAtual") LocalDate dataAtual);

    // Buscar eventos passados
    @Query("SELECT e FROM Evento e WHERE e.data < :dataAtual ORDER BY e.data DESC")
    List<Evento> findEventosPassados(@Param("dataAtual") LocalDate dataAtual);
}
