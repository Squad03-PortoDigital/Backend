package com.squad03.flap.repository;

import com.squad03.flap.model.Anexo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AnexoRepository extends JpaRepository<Anexo, Long> {
}
