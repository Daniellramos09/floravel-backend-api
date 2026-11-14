package br.com.floravel.floravel.domain.triagem.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckInTriagemRequestDTO {

    @NotBlank(message = "O ID da mãe é obrigatório para registrar a triagem.")
    private String maeId;

    @NotEmpty(message = "A lista de respostas não pode ser vazia.")
    @Valid
    private List<ItemRespostaDTO> respostas;
}
