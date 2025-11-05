package com.squad03.flap.service;

import com.squad03.flap.DTO.AtualizacaoItem;
import com.squad03.flap.DTO.BuscaItem;
import com.squad03.flap.DTO.CadastroItem;
import com.squad03.flap.model.Checklist;
import com.squad03.flap.model.Item;
import com.squad03.flap.model.Tarefa;
import com.squad03.flap.repository.ChecklistRepository;
import com.squad03.flap.repository.ItemRepository;
import com.squad03.flap.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final TarefaRepository tarefaRepository;
    private final ChecklistRepository checklistRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository, TarefaRepository tarefaRepository, ChecklistRepository checklistRepository) {
        this.itemRepository = itemRepository;
        this.tarefaRepository = tarefaRepository;
        this.checklistRepository = checklistRepository;
    }

    public BuscaItem cadastrarItem(CadastroItem cadastroItem) {
        Optional<Tarefa> tarefaOpt = tarefaRepository.findById(cadastroItem.tarefaId());
        
        if (tarefaOpt.isEmpty()) {
            throw new RuntimeException("Tarefa não encontrada com o ID: " + cadastroItem.tarefaId());
        }

        Tarefa tarefa = tarefaOpt.get();
        
        // Verificar se checklist existe, se informado
        Checklist checklist = null;
        if (cadastroItem.checklistId() != null) {
            Optional<Checklist> checklistOpt = checklistRepository.findById(cadastroItem.checklistId());
            if (checklistOpt.isEmpty()) {
                throw new RuntimeException("Checklist não encontrado com o ID: " + cadastroItem.checklistId());
            }
            checklist = checklistOpt.get();
        }
        
        Item item = Item.builder()
                .nome(cadastroItem.nome())
                .status(cadastroItem.status() != null ? cadastroItem.status() : false)
                .tarefa(tarefa)
                .checklist(checklist)
                .build();

        Item itemSalvo = itemRepository.save(item);
        return new BuscaItem(itemSalvo);
    }

    public List<BuscaItem> listarItens() {
        return itemRepository.findAll()
                .stream()
                .map(BuscaItem::new)
                .collect(Collectors.toList());
    }

    public List<BuscaItem> listarItensPorTarefa(Long tarefaId) {
        return itemRepository.findByTarefaId(tarefaId)
                .stream()
                .map(BuscaItem::new)
                .collect(Collectors.toList());
    }

    public List<BuscaItem> listarItensPorChecklist(Long checklistId) {
        return itemRepository.findByChecklistId(checklistId)
                .stream()
                .map(BuscaItem::new)
                .collect(Collectors.toList());
    }

    public Optional<BuscaItem> buscarItemPorId(Long id) {
        return itemRepository.findById(id)
                .map(BuscaItem::new);
    }

    public Optional<BuscaItem> atualizarItem(Long id, AtualizacaoItem atualizacaoItem) {
        Optional<Item> itemOpt = itemRepository.findById(id);
        
        if (itemOpt.isEmpty()) {
            return Optional.empty();
        }

        Item item = itemOpt.get();
        
        if (atualizacaoItem.nome() != null && !atualizacaoItem.nome().trim().isEmpty()) {
            item.setNome(atualizacaoItem.nome());
        }
        
        if (atualizacaoItem.status() != null) {
            item.setStatus(atualizacaoItem.status());
        }

        Item itemAtualizado = itemRepository.save(item);
        return Optional.of(new BuscaItem(itemAtualizado));
    }


    public boolean deletarItem(Long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<BuscaItem> alternarStatusItem(Long id) {
        Optional<Item> itemOpt = itemRepository.findById(id);

        if (itemOpt.isEmpty()) {
            return Optional.empty();
        }

        Item item = itemOpt.get();

        // ✅ Alterna o status - use getStatus() e setStatus()
        item.setStatus(!item.getStatus());

        Item itemAtualizado = itemRepository.save(item);
        return Optional.of(new BuscaItem(itemAtualizado));
    }

}