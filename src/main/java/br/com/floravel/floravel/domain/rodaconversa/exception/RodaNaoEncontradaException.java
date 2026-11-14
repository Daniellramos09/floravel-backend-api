package br.com.floravel.floravel.domain.rodaconversa.exception;

public class RodaNaoEncontradaException extends RuntimeException {
    public RodaNaoEncontradaException(String rodaId) {
        super("Roda de conversa não encontrada para o ID: " + rodaId);
    }
}
