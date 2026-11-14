package br.com.floravel.floravel.domain.rodaconversa.exception;

public class MaeJaInscritaException extends RuntimeException {
    public MaeJaInscritaException(String maeId, String rodaId) {
        super("A mãe informada já possui inscrição confirmada nesta roda de conversa.");
    }
}
