package com.squad03.flap.controller;

import com.squad03.flap.DTO.UsuarioEquipeDTO;
import com.squad03.flap.service.EquipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/equipes")
public class EquipeController {

    @Autowired
    private EquipeService equipeService;

    @GetMapping
    public ResponseEntity<Page<UsuarioEquipeDTO>> getEquipe(
            // O Spring vai montar o Pageable a partir dos parâmetros da URL
            // Ex: ?page=0&size=9&sort=nome,asc
            @PageableDefault(size = 9, sort = "nome") Pageable pageable) {

        Page<UsuarioEquipeDTO> equipePage = equipeService.listarEquipePaginado(pageable);
        return ResponseEntity.ok(equipePage);
    }
}
