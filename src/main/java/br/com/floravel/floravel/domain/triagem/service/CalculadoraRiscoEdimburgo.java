package br.com.floravel.floravel.domain.triagem.service;

import br.com.floravel.floravel.domain.triagem.dto.ItemRespostaDTO;
import br.com.floravel.floravel.domain.triagem.exception.TriagemInvalidaException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class CalculadoraRiscoEdimburgo implements CalculadoraRiscoTriagem {

    private static final int QUESTOES_ESCALA_TOTAL = 10;
    private static final int QUESTAO_IDEACAO_SUICIDA = 10;

    @Override
    public ResultadoCalculoTriagem calcular(List<ItemRespostaDTO> respostas) {
        if (respostas == null || respostas.isEmpty()) {
            throw new TriagemInvalidaException("A triagem precisa conter ao menos uma resposta válida.");
        }

        Set<Integer> questoesProcessadas = new HashSet<>();
        int pontuacaoTotal = 0;
        boolean alertaUrgente = false;

        for (ItemRespostaDTO item : respostas) {
            if (!questoesProcessadas.add(item.getNumeroQuestao())) {
                throw new TriagemInvalidaException("Questão duplicada informada no formulário: #" + item.getNumeroQuestao());
            }

            pontuacaoTotal += item.getValorResposta();

            // Protocolo EPDS: Questão 10 refere-se a pensamentos de autoagressão
            if (item.getNumeroQuestao() == QUESTAO_IDEACAO_SUICIDA && item.getValorResposta() > 0) {
                alertaUrgente = true;
            }
        }

        String nivelRisco = classificarNivelRisco(pontuacaoTotal, alertaUrgente);
        String orientacao = gerarOrientacaoClinica(nivelRisco, alertaUrgente);

        return new ResultadoCalculoTriagem(pontuacaoTotal, nivelRisco, alertaUrgente, orientacao);
    }

    private String classificarNivelRisco(int pontuacaoTotal, boolean alertaUrgente) {
        if (alertaUrgente || pontuacaoTotal >= 13) {
            return "ALTO";
        }
        if (pontuacaoTotal >= 10) {
            return "MODERADO";
        }
        return "BAIXO";
    }

    private String gerarOrientacaoClinica(String nivelRisco, boolean alertaUrgente) {
        if (alertaUrgente) {
            return "Identificamos sinais de sofrimento agudo. A Florável recomenda acolhimento profissional prioritário com nossas psicólogas parceiras.";
        }
        return switch (nivelRisco) {
            case "ALTO" -> "Pontuação indicativa de sofrimento emocional significativo. Sugerimos acompanhamento psicológico individual e participação nas rodas de escuta mútua.";
            case "MODERADO" -> "Sintomas leves a moderados de sobrecarga puerperal. Participar das rodas de conversa semanais pode trazer alívio e acolhimento mútuo.";
            default -> "Seu bem-estar está equilibrado. Continue reservando momentos de autocuidado e explorando os conteúdos da nossa biblioteca.";
        };
    }
}
