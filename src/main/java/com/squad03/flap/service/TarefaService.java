package com.squad03.flap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.squad03.flap.model.Tarefa;
import com.squad03.flap.repository.TarefaRepository;

@Service
public class TarefaService {
    
    @Autowired
    private TarefaRepository tarefaRepository;

    public Tarefa CriarTarefa () {
        Tarefa novaTarefa = new Tarefa();
        tarefaRepository.save(novaTarefa);
        return novaTarefa;
    }
    
    public java.util.List<Tarefa> getAllTarefas() {
        return tarefaRepository.findAll();
    }

    public Tarefa getTarefaById(Long id) {
        return tarefaRepository.findById(id).orElse(null);
    }

    public Tarefa updateTarefa(Long id, Tarefa tarefa) {
        tarefa.setId(id);
        return tarefaRepository.save(tarefa);
    }

    public void deleteTarefa(Long id) {
        tarefaRepository.deleteById(id);
    }



}
