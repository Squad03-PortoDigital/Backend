package com.squad03.flap.exception;

public class TarefaValidacaoException extends RuntimeException {
    
    public TarefaValidacaoException(String mensagem) {
        super(mensagem);
    }
    
    public TarefaValidacaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
