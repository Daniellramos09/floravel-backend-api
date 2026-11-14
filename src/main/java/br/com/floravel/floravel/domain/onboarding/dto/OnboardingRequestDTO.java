package br.com.floravel.floravel.domain.onboarding.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnboardingRequestDTO {

    @NotBlank(message = "O ID da mãe é obrigatório.")
    private String maeId;

    @NotBlank(message = "A fase da maternidade deve ser informada.")
    @Size(max = 50, message = "Fase da maternidade não pode ultrapassar 50 caracteres.")
    private String faseMaternidade;

    @NotBlank(message = "O principal desafio deve ser informado.")
    @Size(max = 50, message = "Principal desafio não pode ultrapassar 50 caracteres.")
    private String principalDesafio;

    @NotBlank(message = "A faixa de renda familiar deve ser informada.")
    @Size(max = 50, message = "Faixa de renda não pode ultrapassar 50 caracteres.")
    private String faixaRenda;

    @NotBlank(message = "O valor disposto a pagar na psicologia deve ser informado.")
    @Size(max = 50, message = "Disposição a pagar não pode ultrapassar 50 caracteres.")
    private String dispostoPagarPsicologia;

    @NotBlank(message = "A expectativa principal deve ser informada.")
    @Size(max = 50, message = "Expectativa principal não pode ultrapassar 50 caracteres.")
    private String expectativaPrincipal;
}
