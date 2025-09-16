package com.squad03.flap.repository;

import com.squad03.flap.model.Cargo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Long> {

    // Buscar cargo por nome
    Optional<Cargo> findByNome(String nome);

    // Verificar se existe cargo com o nome
    boolean existsByNome(String nome);

    // Buscar cargos por nome contendo (case insensitive)
    List<Cargo> findByNomeContainingIgnoreCase(String nome);

    // Buscar cargos ordenados por nome
    List<Cargo> findAllByOrderByNomeAsc();

    // Query customizada para buscar cargos com usuários
    @Query("SELECT DISTINCT c FROM Cargo c LEFT JOIN FETCH c.usuarios")
    List<Cargo> findAllWithUsuarios();

    // Contar quantos usuários tem um cargo específico
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.cargo.id = :cargoId")
    Long countUsuariosByCargoId(@Param("cargoId") Long cargoId);
}