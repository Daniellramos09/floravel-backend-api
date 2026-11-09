package br.com.floravel.floravel.domain.mae.exception;

public class MaeNaoEncontradaException extends RuntimeException {

    public MaeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public static MaeNaoEncontradaException porId(String id) {
        return new MaeNaoEncontradaException("Mãe não encontrada com o ID: " + id);
    }

    public static MaeNaoEncontradaException porEmail(String email) {
        return new MaeNaoEncontradaException("Mãe não encontrada com o e-mail: " + email);
    }
}
