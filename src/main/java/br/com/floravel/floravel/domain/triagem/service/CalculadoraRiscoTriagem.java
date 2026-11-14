package br.com.floravel.floravel.domain.triagem.service;

import br.com.floravel.floravel.domain.triagem.dto.ItemRespostaDTO;

import java.util.List;

public interface CalculadoraRiscoTriagem {
    ResultadoCalculoTriagem calcular(List<ItemRespostaDTO> respostas);

    record ResultadoCalculoTriagem(
            int pontuacaoTotal,
            String nivelRisco,
            boolean alertaUrgente,
            String orientacaoClinica
    ) {}
}
