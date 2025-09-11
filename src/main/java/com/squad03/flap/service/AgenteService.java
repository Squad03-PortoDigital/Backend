package com.squad03.flap.service;

import com.squad03.flap.DTO.CadastroAgente;
import com.squad03.flap.DTO.BuscaAgente;
import com.squad03.flap.model.Agente;
import com.squad03.flap.repository.AgenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AgenteService {

    @Autowired
    private AgenteRepository agenteRepository;

    @Transactional
    public BuscaAgente salvarAgente(CadastroAgente dados){
        Agente novoAgente = new Agente(dados.nome(), dados.link(), dados.foto());
        Agente agenteSalvo = agenteRepository.save(novoAgente);
        return new BuscaAgente(agenteSalvo);
    }

    public List<BuscaAgente> listarTodos() {
        return agenteRepository.findAll()
                .stream()
                .map(BuscaAgente::new)
                .collect(Collectors.toList());
    }

    public Optional<BuscaAgente> buscarPorId(int id){
        return agenteRepository.findById(id)
                .map(BuscaAgente::new);
    }

    public void deletarAgente(int id){
        agenteRepository.deleteById(id);
    }

}
