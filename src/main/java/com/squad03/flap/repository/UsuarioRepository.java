package com.squad03.flap.repository;

import com.squad03.flap.model.Cargo;
import com.squad03.flap.model.Usuario;
import com.squad03.flap.model.Role;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar usuário por email
    Optional<Usuario> findByEmail(String email);

    // Verificar se existe usuário com o email
    boolean existsByEmail(String email);

    // Buscar usuários por nome contendo (case insensitive)
    List<Usuario> findByNomeContainingIgnoreCase(String nome);

    // Buscar usuários por role
    List<Usuario> findByRole(Role role);

    // Buscar usuários por role ID
    List<Usuario> findByRoleId(Long roleId);

    // Buscar usuários por cargo
    List<Usuario> findByCargo(Cargo cargo);

    // Buscar usuários por cargo ID
    List<Usuario> findByCargoId(Long cargoId);

    // Buscar usuários por role e cargo
    List<Usuario> findByRoleAndCargo(Role role, Cargo cargo);

    // Buscar usuários ordenados por nome
    List<Usuario> findAllByOrderByNomeAsc();

    // Query para buscar usuário com role e cargo carregados
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.role LEFT JOIN FETCH u.cargo WHERE u.id = :id")
    Optional<Usuario> findByIdWithRoleAndCargo(@Param("id") Long id);

    // Query para buscar todos os usuários com role e cargo carregados
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.role LEFT JOIN FETCH u.cargo")
    List<Usuario> findAllWithRoleAndCargo();

    // Buscar usuário por email com role e cargo carregados
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.role LEFT JOIN FETCH u.cargo WHERE u.email = :email")
    Optional<Usuario> findByEmailWithRoleAndCargo(@Param("email") String email);

    // Query para buscar usuários por nome da role
    @Query("SELECT u FROM Usuario u WHERE u.role.nome = :nomeRole")
    List<Usuario> findByRoleNome(@Param("nomeRole") String nomeRole);

    // Query para buscar usuários por nome do cargo
    @Query("SELECT u FROM Usuario u WHERE u.cargo.nome = :nomeCargo")
    List<Usuario> findByCargoNome(@Param("nomeCargo") String nomeCargo);

    // Query para buscar usuários por nome da role e nome do cargo
    @Query("SELECT u FROM Usuario u WHERE u.role.nome = :nomeRole AND u.cargo.nome = :nomeCargo")
    List<Usuario> findByRoleNomeAndCargoNome(@Param("nomeRole") String nomeRole, @Param("nomeCargo") String nomeCargo);

    // Query para contar usuários por role
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.role = :role")
    Long countByRole(@Param("role") Role role);

    // Query para contar usuários por cargo
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.cargo = :cargo")
    Long countByCargo(@Param("cargo") Cargo cargo);

    // Buscar usuários que possuem foto
    @Query("SELECT u FROM Usuario u WHERE u.foto IS NOT NULL AND u.foto != ''")
    List<Usuario> findUsuariosComFoto();

    // Buscar usuários que não possuem foto
    @Query("SELECT u FROM Usuario u WHERE u.foto IS NULL OR u.foto = ''")
    List<Usuario> findUsuariosSemFoto();
}
