package com.squad03.flap.service;



import com.squad03.flap.model.Role;
import com.squad03.flap.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // Salvar nova role
    public Role salvar(Role role) {
        if (role.getNome() == null || role.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da role não pode ser vazio");
        }

        if (roleRepository.existsByNome(role.getNome())) {
            throw new IllegalArgumentException("Já existe uma role com este nome");
        }

        return roleRepository.save(role);
    }

    // Atualizar role
    public Role atualizar(Role role) {
        if (role.getId() == null) {
            throw new IllegalArgumentException("ID da role não pode ser nulo");
        }

        if (!roleRepository.existsById(role.getId())) {
            throw new IllegalArgumentException("Role não encontrada");
        }

        if (role.getNome() == null || role.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da role não pode ser vazio");
        }

        // Verificar se já existe outra role com o mesmo nome
        Optional<Role> roleExistente = roleRepository.findByNome(role.getNome());
        if (roleExistente.isPresent() && !roleExistente.get().getId().equals(role.getId())) {
            throw new IllegalArgumentException("Já existe uma role com este nome");
        }

        return roleRepository.save(role);
    }

    // Buscar todas as roles
    @Transactional(readOnly = true)
    public List<Role> buscarTodas() {
        return roleRepository.findAllByOrderByNomeAsc();
    }

    // Buscar role por ID
    @Transactional(readOnly = true)
    public Optional<Role> buscarPorId(Long id) {
        return roleRepository.findById(id);
    }

    // Buscar role por nome
    @Transactional(readOnly = true)
    public Optional<Role> buscarPorNome(String nome) {
        return roleRepository.findByNome(nome);
    }

    // Buscar roles por nome contendo
    @Transactional(readOnly = true)
    public List<Role> buscarPorNomeContendo(String nome) {
        return roleRepository.findByNomeContainingIgnoreCase(nome);
    }

    // Buscar roles com usuários
    @Transactional(readOnly = true)
    public List<Role> buscarComUsuarios() {
        return roleRepository.findAllWithUsuarios();
    }

    // Contar usuários por role
    @Transactional(readOnly = true)
    public Long contarUsuarios(Long roleId) {
        return roleRepository.countUsuariosByRoleId(roleId);
    }

    // Deletar role
    public void deletar(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new IllegalArgumentException("Role não encontrada");
        }

        Long quantidadeUsuarios = roleRepository.countUsuariosByRoleId(id);
        if (quantidadeUsuarios > 0) {
            throw new IllegalArgumentException("Não é possível deletar role que possui usuários associados");
        }

        roleRepository.deleteById(id);
    }

    // Verificar se existe
    @Transactional(readOnly = true)
    public boolean existe(Long id) {
        return roleRepository.existsById(id);
    }
}