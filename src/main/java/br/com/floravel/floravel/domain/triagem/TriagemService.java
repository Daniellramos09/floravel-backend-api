package br.com.floravel.floravel.domain.triagem;

import br.com.floravel.floravel.domain.mae.Mae;
import br.com.floravel.floravel.domain.mae.MaeRepository;
import br.com.floravel.floravel.domain.mae.exception.MaeNaoEncontradaException;
import br.com.floravel.floravel.domain.triagem.dto.CheckInTriagemRequestDTO;
import br.com.floravel.floravel.domain.triagem.dto.HistoricoTriagemResponseDTO;
import br.com.floravel.floravel.domain.triagem.dto.ItemRespostaDTO;
import br.com.floravel.floravel.domain.triagem.dto.TriagemResponseDTO;
import br.com.floravel.floravel.domain.triagem.service.CalculadoraRiscoTriagem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TriagemService {

    private final TriagemRepository triagemRepository;
    private final MaeRepository maeRepository;
    private final CalculadoraRiscoTriagem calculadoraRisco;

    @Transactional
    public TriagemResponseDTO registrarCheckIn(CheckInTriagemRequestDTO dto) {
        Mae mae = maeRepository.findById(dto.getMaeId())
                .orElseThrow(() -> MaeNaoEncontradaException.porId(dto.getMaeId()));

        // SRP & DIP: Cálculo e regras clínicas delegados para a estratégia especializada
        CalculadoraRiscoTriagem.ResultadoCalculoTriagem resultado = calculadoraRisco.calcular(dto.getRespostas());

        TriagemEdimburgo triagem = TriagemEdimburgo.builder()
                .mae(mae)
                .pontuacaoTotal(resultado.pontuacaoTotal())
                .nivelRisco(resultado.nivelRisco())
                .build();

        for (ItemRespostaDTO item : dto.getRespostas()) {
            RespostaEdimburgo resposta = RespostaEdimburgo.builder()
                    .numeroQuestao(item.getNumeroQuestao())
                    .valorResposta(item.getValorResposta())
                    .build();
            triagem.adicionarResposta(resposta);
        }

        TriagemEdimburgo salva = triagemRepository.save(triagem);

        return TriagemResponseDTO.fromEntity(salva, resultado.alertaUrgente(), resultado.orientacaoClinica());
    }

    @Transactional(readOnly = true)
    public HistoricoTriagemResponseDTO obterHistoricoPorMae(String maeId) {
        if (!maeRepository.existsById(maeId)) {
            throw MaeNaoEncontradaException.porId(maeId);
        }

        List<TriagemEdimburgo> triagens = triagemRepository.findByMaeIdComRespostas(maeId);

        List<TriagemResponseDTO> dtos = triagens.stream()
                .map(t -> {
                    boolean urgente = "ALTO".equalsIgnoreCase(t.getNivelRisco());
                    String orientacao = urgente
                            ? "Identificamos sinais de sofrimento agudo. Recomendamos acolhimento profissional prioritário."
                            : "Acompanhamento de bem-estar rotineiro.";
                    return TriagemResponseDTO.fromEntity(t, urgente, orientacao);
                })
                .toList();

        TriagemResponseDTO ultima = dtos.isEmpty() ? null : dtos.get(0);

        return HistoricoTriagemResponseDTO.builder()
                .maeId(maeId)
                .totalTriagensRealizadas(dtos.size())
                .ultimaTriagem(ultima)
                .historico(dtos)
                .build();
    }
}
