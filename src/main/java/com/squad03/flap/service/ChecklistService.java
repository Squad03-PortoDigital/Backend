package com.squad03.flap.service;

import com.squad03.flap.DTO.AtualizacaoChecklist;
import com.squad03.flap.DTO.BuscaChecklist;
import com.squad03.flap.DTO.CadastroChecklist;
import com.squad03.flap.model.Checklist;
import com.squad03.flap.model.Tarefa;
import com.squad03.flap.repository.ChecklistRepository;
import com.squad03.flap.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChecklistService {

    private final ChecklistRepository checklistRepository;
    private final TarefaRepository tarefaRepository;

    @Autowired
    public ChecklistService(ChecklistRepository checklistRepository, TarefaRepository tarefaRepository) {
        this.checklistRepository = checklistRepository;
        this.tarefaRepository = tarefaRepository;
    }

    public BuscaChecklist cadastrarChecklist(CadastroChecklist cadastroChecklist) {
        Optional<Tarefa> tarefaOpt = tarefaRepository.findById(cadastroChecklist.tarefaId());
        
        if (tarefaOpt.isEmpty()) {
            throw new RuntimeException("Tarefa não encontrada com o ID: " + cadastroChecklist.tarefaId());
        }

        Tarefa tarefa = tarefaOpt.get();
        
        Checklist checklist = Checklist.builder()
                .titulo(cadastroChecklist.titulo())
                .cor(cadastroChecklist.cor())
                .tarefa(tarefa)
                .build();

        Checklist checklistSalvo = checklistRepository.save(checklist);
        return new BuscaChecklist(checklistSalvo);
    }

    public List<BuscaChecklist> listarChecklists() {
        return checklistRepository.findAll()
                .stream()
                .map(BuscaChecklist::new)
                .collect(Collectors.toList());
    }

    public List<BuscaChecklist> listarChecklistsPorTarefa(Long tarefaId) {
        return checklistRepository.findByTarefaIdWithItens(tarefaId)
                .stream()
                .map(BuscaChecklist::new)
                .collect(Collectors.toList());
    }

    public Optional<BuscaChecklist> buscarChecklistPorId(Long id) {
        Checklist checklist = checklistRepository.findByIdWithItens(id);
        return checklist != null ? Optional.of(new BuscaChecklist(checklist)) : Optional.empty();
    }

    public Optional<BuscaChecklist> atualizarChecklist(Long id, AtualizacaoChecklist atualizacaoChecklist) {
        Optional<Checklist> checklistOpt = checklistRepository.findById(id);
        
        if (checklistOpt.isEmpty()) {
            return Optional.empty();
        }

        Checklist checklist = checklistOpt.get();
        
        if (atualizacaoChecklist.titulo() != null && !atualizacaoChecklist.titulo().trim().isEmpty()) {
            checklist.setTitulo(atualizacaoChecklist.titulo());
        }
        
        if (atualizacaoChecklist.cor() != null && !atualizacaoChecklist.cor().trim().isEmpty()) {
            checklist.setCor(atualizacaoChecklist.cor());
        }

        Checklist checklistAtualizado = checklistRepository.save(checklist);
        return Optional.of(new BuscaChecklist(checklistAtualizado));
    }

    public boolean deletarChecklist(Long id) {
        if (checklistRepository.existsById(id)) {
            checklistRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<BuscaChecklist> buscarChecklistComItens(Long id) {
        Checklist checklist = checklistRepository.findByIdWithItens(id);
        return checklist != null ? Optional.of(new BuscaChecklist(checklist)) : Optional.empty();
    }
}