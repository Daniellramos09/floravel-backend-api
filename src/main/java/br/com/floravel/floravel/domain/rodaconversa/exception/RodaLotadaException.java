package br.com.floravel.floravel.domain.rodaconversa.exception;

public class RodaLotadaException extends RuntimeException {
    public RodaLotadaException(String tema, int limite) {
        super("A roda de conversa '" + tema + "' atingiu o limite máximo de " + limite + " participantes.");
    }
}
