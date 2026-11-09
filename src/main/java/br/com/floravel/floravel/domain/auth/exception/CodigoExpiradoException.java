package br.com.floravel.floravel.domain.auth.exception;

public class CodigoExpiradoException extends RuntimeException {
    public CodigoExpiradoException(String mensagem) {
        super(mensagem);
    }
}
