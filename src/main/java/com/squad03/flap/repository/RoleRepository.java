package com.squad03.flap.repository;

import com.squad03.flap.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Buscar role por nome
    Optional<Role> findByNome(String nome);

    // Verificar se existe role com o nome
    boolean existsByNome(String nome);

    // Buscar roles por nome contendo (case insensitive)
    List<Role> findByNomeContainingIgnoreCase(String nome);

    // Buscar roles ordenadas por nome
    List<Role> findAllByOrderByNomeAsc();

    // Query customizada para buscar roles com usuários
    @Query("SELECT DISTINCT r FROM Role r LEFT JOIN FETCH r.usuarios")
    List<Role> findAllWithUsuarios();

    // Contar quantos usuários tem uma role específica
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.role.id = :roleId")
    Long countUsuariosByRoleId(@Param("roleId") Long roleId);
}