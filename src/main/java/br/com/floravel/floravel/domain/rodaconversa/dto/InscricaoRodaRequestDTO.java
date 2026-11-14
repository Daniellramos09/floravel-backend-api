package br.com.floravel.floravel.domain.rodaconversa.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscricaoRodaRequestDTO {

    @NotBlank(message = "O ID da mãe é obrigatório para realizar a inscrição.")
    private String maeId;
}
