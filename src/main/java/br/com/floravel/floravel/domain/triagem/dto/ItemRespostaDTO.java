package br.com.floravel.floravel.domain.triagem.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRespostaDTO {

    @NotNull(message = "O número da questão é obrigatório.")
    @Min(value = 1, message = "O número da questão deve ser entre 1 e 10.")
    @Max(value = 10, message = "O número da questão deve ser entre 1 e 10.")
    private Integer numeroQuestao;

    @NotNull(message = "O valor da resposta é obrigatório.")
    @Min(value = 0, message = "O valor da resposta deve ser no mínimo 0.")
    @Max(value = 3, message = "O valor da resposta deve ser no máximo 3.")
    private Integer valorResposta;
}
