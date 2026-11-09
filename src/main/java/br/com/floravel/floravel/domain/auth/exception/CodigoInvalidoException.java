package br.com.floravel.floravel.domain.auth.exception;

public class CodigoInvalidoException extends RuntimeException {
    public CodigoInvalidoException(String mensagem) {
        super(mensagem);
    }
}
