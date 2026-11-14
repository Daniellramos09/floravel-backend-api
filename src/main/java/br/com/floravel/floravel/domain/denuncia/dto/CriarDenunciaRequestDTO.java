package br.com.floravel.floravel.domain.denuncia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CriarDenunciaRequestDTO {

    @NotBlank(message = "O ID do usuário denunciante é obrigatório.")
    private String denuncianteUsuarioId;

    private String denunciadoUsuarioId;

    private String salaId;

    @NotBlank(message = "O motivo da denúncia deve ser informado.")
    @Size(max = 50, message = "Motivo não pode ultrapassar 50 caracteres.")
    private String motivo;

    @NotBlank(message = "Descreva detalhadamente o que a pessoa fez ou disse.")
    private String oQueFez;

    @NotBlank(message = "Descreva como você se sentiu com essa situação.")
    private String oQueSentiu;

    @Builder.Default
    private Boolean bloquearUsuario = true;
}
