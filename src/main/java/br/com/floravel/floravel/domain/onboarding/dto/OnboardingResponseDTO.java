package br.com.floravel.floravel.domain.onboarding.dto;

import br.com.floravel.floravel.domain.onboarding.OnboardingMae;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnboardingResponseDTO {

    private String id;
    private String maeId;
    private String nomeMae;
    private String faseMaternidade;
    private String principalDesafio;
    private String faixaRenda;
    private String dispostoPagarPsicologia;
    private String expectativaPrincipal;
    private String temaRodaPrioritario;
    private String mensagemAcolhimento;
    private LocalDateTime dataResposta;

    public static OnboardingResponseDTO fromEntity(OnboardingMae entity, String temaPrioritario, String mensagemAcolhimento) {
        return OnboardingResponseDTO.builder()
                .id(entity.getId())
                .maeId(entity.getMae().getId())
                .nomeMae(entity.getMae().getUsuario() != null ? entity.getMae().getUsuario().getNome() : null)
                .faseMaternidade(entity.getFaseMaternidade())
                .principalDesafio(entity.getPrincipalDesafio())
                .faixaRenda(entity.getFaixaRenda())
                .dispostoPagarPsicologia(entity.getDispostoPagarPsicologia())
                .expectativaPrincipal(entity.getExpectativaPrincipal())
                .temaRodaPrioritario(temaPrioritario)
                .mensagemAcolhimento(mensagemAcolhimento)
                .dataResposta(entity.getDataResposta())
                .build();
    }
}
