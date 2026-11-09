package br.com.floravel.floravel.domain.mae.exception;

public class EmailJaCadastradoException extends RuntimeException {

    public EmailJaCadastradoException(String email) {
        super("O e-mail '" + email + "' já está cadastrado na plataforma Florável.");
    }
}
