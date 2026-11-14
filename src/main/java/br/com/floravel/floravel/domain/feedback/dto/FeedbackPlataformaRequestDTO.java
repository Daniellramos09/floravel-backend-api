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
public class FeedbackPlataformaRequestDTO {

    @NotBlank(message = "O ID do usuário é obrigatório.")
    private String usuarioId;

    @NotBlank(message = "A categoria deve ser informada (ex: 'Rodas de Conversa', 'Consultas', 'Plataforma').")
    private String categoria;

    @NotNull(message = "A nota de 1 a 5 é obrigatória.")
    @Min(value = 1, message = "A nota mínima é 1.")
    @Max(value = 5, message = "A nota máxima é 5.")
    private Integer nota;

    @NotBlank(message = "A mensagem ou relato do feedback não pode ser vazia.")
    private String mensagem;
}
