package com.squad03.flap.DTO;

import java.util.Optional;
import java.util.Set;

public record AtualizacaoRole(

        String nome,

        Set<Long> permissoesIds
) {}