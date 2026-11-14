package br.com.floravel.floravel.domain.feedback.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackReuniaoRequestDTO {

    @NotBlank(message = "O ID da sala de reunião é obrigatório.")
    private String salaId;

    @NotBlank(message = "O ID do usuário participante é obrigatório.")
    private String usuarioId;

    private String expectativaPre;

    @NotNull(message = "A nota de satisfação de 1 a 5 estrelas é obrigatória.")
    @Min(value = 1, message = "A nota mínima é 1 estrela.")
    @Max(value = 5, message = "A nota máxima é 5 estrelas.")
    private Integer notaSatisfacao;

    @NotBlank(message = "Informe se a reunião atendeu às suas expectativas ('sim', 'parcialmente', 'nao').")
    private String atendeuExpectativa;

    @NotBlank(message = "Por favor, compartilhe brevemente o que achou da reunião.")
    private String oQueAchouTexto;

    @Builder.Default
    private Boolean permitirDivulgacaoAnonima = true;
}
