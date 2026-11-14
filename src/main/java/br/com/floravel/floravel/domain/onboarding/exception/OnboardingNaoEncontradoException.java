package br.com.floravel.floravel.domain.onboarding.exception;

public class OnboardingNaoEncontradoException extends RuntimeException {
    public OnboardingNaoEncontradoException(String maeId) {
        super("Questionário inicial de personalização não encontrado para a mãe com ID: " + maeId);
    }
}
