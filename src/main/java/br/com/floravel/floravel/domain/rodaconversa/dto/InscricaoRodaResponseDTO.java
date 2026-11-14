package br.com.floravel.floravel.domain.rodaconversa.dto;

import br.com.floravel.floravel.domain.rodaconversa.ParticipanteRoda;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscricaoRodaResponseDTO {

    private String id;
    private String rodaId;
    private String temaRoda;
    private LocalDateTime dataHoraInicio;
    private String maeId;
    private String statusParticipacao;
    private String linkSessao;
    private LocalDateTime dataInscricao;

    public static InscricaoRodaResponseDTO fromEntity(ParticipanteRoda entity) {
        return InscricaoRodaResponseDTO.builder()
                .id(entity.getId())
                .rodaId(entity.getRoda().getId())
                .temaRoda(entity.getRoda().getTema())
                .dataHoraInicio(entity.getRoda().getDataHoraInicio())
                .maeId(entity.getMae().getId())
                .statusParticipacao(entity.getStatusParticipacao())
                .linkSessao(entity.getRoda().getLinkSessao())
                .dataInscricao(entity.getDataInscricao())
                .build();
    }
}
