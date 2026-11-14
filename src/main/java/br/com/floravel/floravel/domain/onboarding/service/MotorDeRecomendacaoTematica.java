package br.com.floravel.floravel.domain.onboarding.service;

import org.springframework.stereotype.Component;

@Component
public class MotorDeRecomendacaoTematica implements MotorDeRecomendacao {

    @Override
    public ResultadoRecomendacao recomendar(String principalDesafio, String faseMaternidade) {
        String tema = mapearTema(principalDesafio);
        String acolhimento = formatarMensagem(principalDesafio, faseMaternidade);
        return new ResultadoRecomendacao(tema, acolhimento);
    }

    private String mapearTema(String desafio) {
        if (desafio == null) return "Acolhimento Geral";
        String lower = desafio.toLowerCase();

        if (lower.contains("depress") || lower.contains("tristeza") || lower.contains("vazio")) {
            return "Depressão Pós-Parto";
        }
        if (lower.contains("sobrecarga") || lower.contains("burnout") || lower.contains("exausta") || lower.contains("cansaço")) {
            return "Sobrecarga & Burnout";
        }
        if (lower.contains("sono") || lower.contains("ansiedade") || lower.contains("medo") || lower.contains("insônia")) {
            return "Ansiedade & Sono";
        }
        if (lower.contains("primeira") || lower.contains("gestante") || lower.contains("iniciante")) {
            return "Mães de 1ª Viagem";
        }
        return "Acolhimento Geral";
    }

    private String formatarMensagem(String desafio, String fase) {
        String faseFormatada = (fase != null && !fase.isBlank()) ? fase : "sua maternidade";
        String desafioFormatado = (desafio != null && !desafio.isBlank()) ? desafio : "seu bem-estar";

        return "Seu espaço foi personalizado com carinho para acolher o momento em que você está (" + faseFormatada +
               "). Destacamos rodas de apoio e profissionais com foco em " + desafioFormatado + ".";
    }
}
