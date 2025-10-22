package com.squad03.flap.service;

import com.squad03.flap.DTO.*;
import com.squad03.flap.model.Permissao;
import com.squad03.flap.model.Role;
import com.squad03.flap.repository.RoleRepository;
import com.squad03.flap.repository.PermissaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.HashSet;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissaoRepository permissaoRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository, PermissaoRepository permissaoRepository) {
        this.roleRepository = roleRepository;
        this.permissaoRepository = permissaoRepository;
    }

    @Transactional
    public BuscaRole cadastrarRole(CadastroRole cadastroRole) {
        List<Permissao> permissoes = permissaoRepository.findAllById(cadastroRole.permissoesIds());

        if (permissoes.size() != cadastroRole.permissoesIds().size()) {
            throw new IllegalArgumentException("Uma ou mais permissões não foram encontradas.");
        }
        Role novaRole = new Role();
        novaRole.setNome(cadastroRole.nome());
        novaRole.setPermissoes(new HashSet<>(permissoes));
        Role roleSalva = roleRepository.save(novaRole);
        return new BuscaRole(roleSalva);
    }

    public Optional<BuscaRole> buscaRolePorId(Long id) {
        return roleRepository.findById(id).map(BuscaRole::new);
    }

    public List<BuscaRole> buscarRoles() {
        return roleRepository.findAll().stream().map(BuscaRole::new).collect(Collectors.toList());
    }

    @Transactional
    public Optional<BuscaRole> atualizarRole(Long id, AtualizacaoRole atualizacaoRole) {
        return roleRepository.findById(id)
                .map(role -> {
                    // Atualiza o NOME, se fornecido no DTO
                    if (atualizacaoRole.nome() != null) {
                        role.setNome(atualizacaoRole.nome());
                    }

                    if (atualizacaoRole.permissoesIds() != null) {
                        List<Permissao> novasPermissoes = permissaoRepository.findAllById(atualizacaoRole.permissoesIds());

                        role.setPermissoes(new HashSet<>(novasPermissoes));
                    }

                    Role roleAtualizada = roleRepository.save(role);
                    return new BuscaRole(roleAtualizada);
                });
    }

    @Transactional
    public boolean deletarRole(Long id){
        if(roleRepository.existsById(id)){
            roleRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
