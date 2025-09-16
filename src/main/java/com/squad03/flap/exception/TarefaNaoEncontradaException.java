package com.squad03.flap.exception;

public class TarefaNaoEncontradaException extends RuntimeException {
    
    public TarefaNaoEncontradaException(Long id) {
        super("Tarefa não encontrada com ID: " + id);
    }
    
    public TarefaNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
