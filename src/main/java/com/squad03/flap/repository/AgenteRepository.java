package com.squad03.flap.repository;

import com.squad03.flap.model.Agente;
import com.squad03.flap.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgenteRepository extends JpaRepository<Agente, Integer> {
}
