package com.squad03.flap.service;

import com.squad03.flap.DTO.PermissaoDTO;
import com.squad03.flap.repository.PermissaoRepository;
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

    @Transactional
    public List<PermissaoDTO> listarPermissoes(){
        return permissaoRepository.findAll()
                .stream()
                .map(PermissaoDTO::new)
                .collect(Collectors.toList());
    }
}
