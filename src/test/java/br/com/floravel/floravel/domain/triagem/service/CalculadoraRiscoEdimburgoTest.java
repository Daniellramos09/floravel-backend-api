package br.com.floravel.floravel.domain.triagem.service;

import br.com.floravel.floravel.domain.triagem.dto.ItemRespostaDTO;
import br.com.floravel.floravel.domain.triagem.exception.TriagemInvalidaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalculadoraRiscoEdimburgoTest {

    private CalculadoraRiscoEdimburgo calculadora;

    @BeforeEach
    void setUp() {
        calculadora = new CalculadoraRiscoEdimburgo();
    }

    @Test
    @DisplayName("Deve classificar risco BAIXO quando pontuação total for menor que 10 e questão 10 for zero")
    void deveClassificarRiscoBaixo() {
        List<ItemRespostaDTO> respostas = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            respostas.add(new ItemRespostaDTO(i, 0)); // Total = 0
        }

        var resultado = calculadora.calcular(respostas);

        assertEquals(0, resultado.pontuacaoTotal());
        assertEquals("BAIXO", resultado.nivelRisco());
        assertFalse(resultado.alertaUrgente());
    }

    @Test
    @DisplayName("Deve classificar risco MODERADO quando pontuação total estiver entre 10 e 12 e questão 10 for zero")
    void deveClassificarRiscoModerado() {
        List<ItemRespostaDTO> respostas = new ArrayList<>();
        // 9 questões com valor 1 (total 9) + questão 10 com 0 = 9... vamos colocar total 11
        for (int i = 1; i <= 8; i++) respostas.add(new ItemRespostaDTO(i, 1));
        respostas.add(new ItemRespostaDTO(9, 3)); // total = 8 + 3 = 11
        respostas.add(new ItemRespostaDTO(10, 0));

        var resultado = calculadora.calcular(respostas);

        assertEquals(11, resultado.pontuacaoTotal());
        assertEquals("MODERADO", resultado.nivelRisco());
        assertFalse(resultado.alertaUrgente());
    }

    @Test
    @DisplayName("Deve classificar risco ALTO e acionar alerta urgente se questão 10 for maior que zero")
    void deveAcionarAlertaUrgenteSeQuestao10MaiorQueZero() {
        List<ItemRespostaDTO> respostas = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            respostas.add(new ItemRespostaDTO(i, 0));
        }
        respostas.add(new ItemRespostaDTO(10, 1)); // Total = 1, mas questão 10 > 0

        var resultado = calculadora.calcular(respostas);

        assertEquals(1, resultado.pontuacaoTotal());
        assertEquals("ALTO", resultado.nivelRisco());
        assertTrue(resultado.alertaUrgente());
    }

    @Test
    @DisplayName("Deve lançar TriagemInvalidaException quando houver questões duplicadas")
    void deveRejeitarQuestoesDuplicadas() {
        List<ItemRespostaDTO> respostas = List.of(
                new ItemRespostaDTO(1, 2),
                new ItemRespostaDTO(1, 3)
        );

        assertThrows(TriagemInvalidaException.class, () -> calculadora.calcular(respostas));
    }
}
