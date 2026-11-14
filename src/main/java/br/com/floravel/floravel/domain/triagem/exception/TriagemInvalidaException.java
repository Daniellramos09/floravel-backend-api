package br.com.floravel.floravel.domain.triagem.exception;

public class TriagemInvalidaException extends RuntimeException {
    public TriagemInvalidaException(String mensagem) {
        super(mensagem);
    }
}
