package com.squad03.flap.repository;

import com.squad03.flap.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    // ✅ Buscar apenas empresas ativas (não arquivadas)
    List<Empresa> findByArquivadaFalse();

    // ✅ Buscar apenas empresas arquivadas
    List<Empresa> findByArquivadaTrue();

    // ✅ Buscar por status de arquivamento
    List<Empresa> findByArquivada(Boolean arquivada);
}
