package com.squad03.flap.service;

import com.squad03.flap.DTO.UsuarioEquipeDTO;
import com.squad03.flap.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EquipeService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public Page<UsuarioEquipeDTO> listarEquipePaginado(Pageable pageable) {
        return usuarioRepository.findEquipeComContagemTarefas(pageable);
    }
}
