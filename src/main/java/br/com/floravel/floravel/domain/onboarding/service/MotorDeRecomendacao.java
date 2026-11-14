package br.com.floravel.floravel.domain.onboarding.service;

public interface MotorDeRecomendacao {
    ResultadoRecomendacao recomendar(String principalDesafio, String faseMaternidade);

    record ResultadoRecomendacao(
            String temaPrioritario,
            String mensagemAcolhimento
    ) {}
}
