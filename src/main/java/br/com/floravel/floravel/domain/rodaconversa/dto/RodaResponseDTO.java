package br.com.floravel.floravel.domain.rodaconversa.dto;

import br.com.floravel.floravel.domain.rodaconversa.RodaDeConversa;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RodaResponseDTO {

    private String id;
    private String tema;
    private LocalDateTime dataHoraInicio;
    private Integer duracaoMinutos;
    private Integer limiteParticipantes;
    private Integer totalInscritos;
    private Integer vagasRestantes;
    private String status;
    private String linkSessao;
    private String psicologoId;
    private String psicologoNome;
    private String psicologoCrp;
    private String psicologoFotoUrl;

    public static RodaResponseDTO fromEntity(RodaDeConversa entity, int totalInscritos) {
        int vagas = Math.max(0, entity.getLimiteParticipantes() - totalInscritos);
        var psi = entity.getPsicologo();
        var user = psi != null ? psi.getUsuario() : null;

        return RodaResponseDTO.builder()
                .id(entity.getId())
                .tema(entity.getTema())
                .dataHoraInicio(entity.getDataHoraInicio())
                .duracaoMinutos(entity.getDuracaoMinutos())
                .limiteParticipantes(entity.getLimiteParticipantes())
                .totalInscritos(totalInscritos)
                .vagasRestantes(vagas)
                .status(entity.getStatus())
                .linkSessao(entity.getLinkSessao())
                .psicologoId(psi != null ? psi.getId() : null)
                .psicologoNome(user != null ? user.getNome() : null)
                .psicologoCrp(psi != null ? psi.getCrp() : null)
                .psicologoFotoUrl(psi != null ? psi.getFotoUrl() : (user != null ? user.getFotoUrl() : null))
                .build();
    }
}
