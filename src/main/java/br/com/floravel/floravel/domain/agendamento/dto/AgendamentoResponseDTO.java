package br.com.floravel.floravel.domain.agendamento.dto;

import br.com.floravel.floravel.domain.agendamento.Agendamento;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendamentoResponseDTO {

    private String id;
    private String maeId;
    private String psicologoId;
    private String psicologoNome;
    private String psicologoCrp;
    private String psicologoFotoUrl;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private String status;
    private String linkSessao;
    private String observacoes;

    public static AgendamentoResponseDTO fromEntity(Agendamento entity) {
        var psi = entity.getPsicologo();
        var user = psi != null ? psi.getUsuario() : null;

        return AgendamentoResponseDTO.builder()
                .id(entity.getId())
                .maeId(entity.getMae().getId())
                .psicologoId(psi != null ? psi.getId() : null)
                .psicologoNome(user != null ? user.getNome() : "Psicóloga Parceira")
                .psicologoCrp(psi != null ? psi.getCrp() : null)
                .psicologoFotoUrl(psi != null ? psi.getFotoUrl() : null)
                .dataHoraInicio(entity.getDataHoraInicio())
                .dataHoraFim(entity.getDataHoraFim())
                .status(entity.getStatus())
                .linkSessao(entity.getLinkSessao())
                .observacoes(entity.getObservacoes())
                .build();
    }
}
