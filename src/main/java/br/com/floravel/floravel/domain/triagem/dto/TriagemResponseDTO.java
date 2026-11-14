package br.com.floravel.floravel.domain.triagem.dto;

import br.com.floravel.floravel.domain.triagem.TriagemEdimburgo;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TriagemResponseDTO {

    private String id;
    private String maeId;
    private String nomeMae;
    private Integer pontuacaoTotal;
    private String nivelRisco;
    private boolean alertaUrgente;
    private String orientacaoClinica;
    private LocalDateTime dataRealizacao;
    private List<ItemRespostaDTO> respostas;

    public static TriagemResponseDTO fromEntity(TriagemEdimburgo entity, boolean alertaUrgente, String orientacao) {
        List<ItemRespostaDTO> itens = entity.getRespostas() != null
                ? entity.getRespostas().stream()
                .map(r -> ItemRespostaDTO.builder()
                        .numeroQuestao(r.getNumeroQuestao())
                        .valorResposta(r.getValorResposta())
                        .build())
                .toList()
                : List.of();

        return TriagemResponseDTO.builder()
                .id(entity.getId())
                .maeId(entity.getMae().getId())
                .nomeMae(entity.getMae().getUsuario() != null ? entity.getMae().getUsuario().getNome() : null)
                .pontuacaoTotal(entity.getPontuacaoTotal())
                .nivelRisco(entity.getNivelRisco())
                .alertaUrgente(alertaUrgente)
                .orientacaoClinica(orientacao)
                .dataRealizacao(entity.getDataRealizacao())
                .respostas(itens)
                .build();
    }
}
