package com.squad03.flap.repository;

import com.squad03.flap.DTO.UsuarioEquipeDTO;
import com.squad03.flap.model.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // Buscar usuários ordenados por nome
    List<Usuario> findAllByOrderByNomeAsc();

    // Buscar usuários que possuem foto
    @Query("SELECT u FROM Usuario u WHERE u.foto IS NOT NULL AND u.foto != ''")
    List<Usuario> findUsuariosComFoto();

    // Buscar usuários que não possuem foto
    @Query("SELECT u FROM Usuario u WHERE u.foto IS NULL OR u.foto = ''")
    List<Usuario> findUsuariosSemFoto();

    // ✅ CORRIGIDO: Removido c.nome e adicionado r.nome para Role
    @Query("SELECT new com.squad03.flap.DTO.UsuarioEquipeDTO(" +
            "   u.id, " +
            "   u.nome, " +
            "   u.email, " +
            "   r.nome, " +  // ✅ Mudou de c.nome para r.nome (Role)
            "   u.foto, " +
            "   COUNT(m) " +
            ") " +
            "FROM Usuario u " +
            "LEFT JOIN u.role r " +  // ✅ Mudou de u.cargo c para u.role r
            "LEFT JOIN u.membros m " +
            "GROUP BY u.id, u.nome, u.email, r.nome, u.foto")
    Page<UsuarioEquipeDTO> findEquipeComContagemTarefas(Pageable pageable);
}
