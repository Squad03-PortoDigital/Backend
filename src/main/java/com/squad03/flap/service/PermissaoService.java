package com.squad03.flap.service;

import com.squad03.flap.DTO.PermissaoDTO;
import com.squad03.flap.model.Usuario;
import com.squad03.flap.repository.PermissaoRepository;
import com.squad03.flap.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PermissaoService {

    @Autowired
    private PermissaoRepository permissaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public List<PermissaoDTO> listarPermissoes(){
        return permissaoRepository.findAll()
                .stream()
                .map(PermissaoDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PermissaoDTO> buscarPermissoesDoUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + usuarioId));

        if (usuario.getRole() == null) {
            throw new RuntimeException("Usuário não tem role atribuída");
        }

        return usuario.getRole().getPermissoes()
                .stream()
                .map(PermissaoDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<String> buscarNomesPermissoesDoUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + usuarioId));

        if (usuario.getRole() == null) {
            throw new RuntimeException("Usuário não tem role atribuída");
        }

        return usuario.getRole().getPermissoes()
                .stream()
                .map(permissao -> permissao.getNome())
                .collect(Collectors.toList());
    }
}
